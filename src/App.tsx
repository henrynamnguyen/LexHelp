import { Component, createSignal } from "solid-js";
import { createTheme, ThemeProvider } from "@suid/material/styles";

import Editor from "./components/Editor";
import Home from "./components/Home";

const theme = createTheme({
  breakpoints: {
    values: {
      xl: 1700,
    },
  },
});

const [page, setPage] = createSignal("home");
const [file, setFile] = createSignal<File | null>(null);

const App: Component = () => {
  return (
    <ThemeProvider theme={theme}>
      {page() === "home" && (
        <Home
          onUpload={(file) => {
            setPage("annotate");
            setFile(file);
          }}
        />
      )}
      {page() === "annotate" && file() && <Editor file={file()!!} />}
    </ThemeProvider>
  );
};

export default App;
