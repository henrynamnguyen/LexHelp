import { GetDocumentParameters } from "pdfjs-dist/types/src/display/api";
import { Component, createEffect, createSignal } from "solid-js";

import * as pdfjsLib from "pdfjs-dist";

export type FieldStatus = "pending" | "errored" | "active" | "filled";

interface RenderProps {
  document: GetDocumentParameters;
  page?: number;
  container: HTMLCanvasElement;

  loadAnnotations: (annotatins: any[]) => void;
  fieldStatus: FieldStatus[];

  activate: (idx: number, annotation: any) => void;
}

const scale = 1.5;
pdfjsLib.GlobalWorkerOptions.workerSrc =
  "../node_modules/pdfjs-dist/build/pdf.worker.js";

const statusColor = (status?: FieldStatus) => {
  switch (status) {
    default:
    case "pending":
      return null;
    case "errored":
      return "#E88585";
    case "active":
      return "#F2F276";
    case "filled":
      return "#9FE885";
  }
};

const [pdf, setPDF] = createSignal<pdfjsLib.PDFDocumentProxy | null>(null);
const [page, setPage] = createSignal<pdfjsLib.PDFPageProxy | null>(null);
const [context, setContext] = createSignal<CanvasRenderingContext2D | null>(
  null
);
const [annotations, setAnnotations] = createSignal<any[]>([]);
const [rects, setRects] = createSignal<any[]>([]);

const Render: Component<RenderProps> = (props) => {
  createEffect(async () => {
    try {
      const pdf = await pdfjsLib.getDocument(props.document).promise;
      setPDF(pdf);
    } catch (error) {
      alert(error);
    }
  });

  const collides = (x: number, y: number) => {
    rects().forEach((rect) => {
      const left = rect.x,
        right = rect.x + rect.w;
      const top = rect.y,
        bottom = rect.y + rect.h;

      if (right >= x && left <= x && bottom >= y && top <= y) {
        props.activate(rect.idx, annotations()[rect.idx]);
      }
    });
  };

  const onClick = (e: any) => {
    collides(e.offsetX, e.offsetY);
  };

  createEffect(() => {
    props.container.addEventListener("click", onClick);

    return () => {
      props.container.removeEventListener("click", onClick);
    };
  });

  createEffect(async () => {
    if (!props.container || !pdf()) {
      return;
    }

    try {
      const page = await pdf()?.getPage(props.page ?? 1);
      if (!page) {
        throw new Error("could not fetch page " + props.page);
      }
      setPage(page);

      const ctx = props.container.getContext("2d")!!;
      setContext(ctx);

      const viewport = page.getViewport({ scale: scale });
      props.container.height = viewport.height;
      props.container.width = viewport.width;

      await page.render({
        canvasContext: ctx,
        viewport,
      }).promise;

      const annotations = await page.getAnnotations();
      setAnnotations(annotations);
      props.loadAnnotations(annotations);
    } catch (error) {
      alert(error);
    }
  });

  createEffect(() => {
    const rects: any[] = [];
    annotations().forEach((a, i) => {
      const vals = a.rect;
      const ctx = context()!!;
      const pdfPage = page()!!;

      const viewport = pdfPage.getViewport({ scale: scale });
      ctx.beginPath();

      const color = statusColor(props.fieldStatus[i]);
      if (color) {
        ctx.fillStyle = color;
        ctx.fillRect(
          vals[0] * scale,
          viewport.height - vals[3] * scale,
          (vals[2] - vals[0]) * scale,
          (vals[3] - vals[1]) * scale
        );
        ctx.stroke();
      }

      rects.push({
        idx: i,
        x: vals[0] * scale,
        y: viewport.height - vals[3] * scale,
        w: (vals[2] - vals[0]) * scale,
        h: (vals[3] - vals[1]) * scale,
      });
    });
    setRects(rects);
  });
  return <></>;
};

export default Render;
