import { Component, createEffect, createSignal } from "solid-js";
import es from "../editor.module.css";
import styles from "../App.module.css";
import Render from "./Render";
import navbar from "../assets/navbar.png";
import Pencil from "./Pencil";
import Form from "./Form";
import Send from "./Send";

interface EditorProps {
  file: File;
}

const [data, setData] = createSignal<any>({});
const [state, setState] = createSignal<"new" | number>("new");
const [annotation, setAnnotation] = createSignal<any>(null);

const Editor: Component<EditorProps> = (props) => {
  let container: HTMLCanvasElement;

  const [buf, setBuf] = createSignal<Uint8Array | null>(null);
  const [fieldStatus, setFieldStatus] = createSignal<any[]>([]);

  const load = (annotations: any[]) => {
    setFieldStatus(annotations.map(() => "pending"));
  };

  const activate = (idx: number, an: any) => {
    const old = fieldStatus();
    if (state() == idx) {
      return;
    }

    if (state() !== "new") {
      if (data()[annotation().fieldName]?.text ?? false) {
        old[state() as number] = "filled";
      } else {
        old[state() as number] = "errored";
      }
    }

    old[idx] = "active";
    setFieldStatus([...old]);

    setAnnotation(an);
    setState(idx);
  };

  createEffect(() => {
    const reader = new FileReader();
    reader.readAsArrayBuffer(props.file);
    reader.onloadend = (e) => {
      if (e.target?.readyState !== FileReader.DONE) return;
      const buf = new Uint8Array(e.target.result as ArrayBuffer);
      setBuf(buf);
    };
  });

  const [show, setShow] = createSignal(false);

  const showModal = () => {
    setShow(true);
  };

  return (
    <>
      <img src={navbar} alt="" style={{ width: "1920px" }} />

      <input
        type="submit"
        value="Complete"
        onClick={showModal}
        style={{
          width: "140px",
          border: "none",
          color: "#2C8793",
          "font-weight": "700",
          background: "#F3F3F3",
          height: "36px",
          "border-radius": "48px",
          position: "absolute",
          top: "30px",
          right: "30px",
        }}
      />

      <div class={styles.Editor}>
        <div class={es.Left}>
          <canvas ref={container!!} class={styles.container}></canvas>
          {buf() && (
            <Render
              document={buf()!!}
              page={1}
              container={container!!}
              loadAnnotations={load}
              fieldStatus={fieldStatus()}
              activate={activate}
            />
          )}
        </div>
        <div class={es.Right}>
          {state() === "new" ? (
            <Pencil />
          ) : (
            <Form data={data()} annotation={annotation()} />
          )}
        </div>
      </div>
      <Send open={show()} />
    </>
  );
};

export default Editor;
