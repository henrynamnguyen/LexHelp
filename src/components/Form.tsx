import Box from "@suid/material/Box";
import TextField from "@suid/material/TextField";
import { Component, createEffect, createSignal } from "solid-js";

interface FormProps {
  data: any;
  annotation: any;
}

const Form: Component<FormProps> = (props) => {
  const [value, setValue] = createSignal<string>("");

  createEffect(() => {
    setValue(props.data[props.annotation.fieldName]?.text ?? "");
  });

  const onChange = (e: any) => {
    setValue(e.target.value);
    props.data[props.annotation.fieldName] = { text: e.target.value };
  };

  return (
    <div
      style={{
        height: "100%",
        display: "flex",
        "flex-direction": "column",
        "justify-content": "center",
        "align-items": "center",
        color: "#5F5F5F",
      }}
    >
      <Box
        component="form"
        onSubmit={(e) => e.preventDefault()}
        sx={{
          "& > :not(style)": { m: 1, width: "30em" },
          display: "flex",
          flexDirection: "column",
          alignItems: "flex-end",
        }}
        noValidate
        autocomplete="off"
      >
        <TextField
          variant="filled"
          InputLabelProps={{ sx: { textAlign: "left", fontSize: "18px" } }}
          label="Add a question for this field."
          placeholder="Enter your prompt in a form of a question"
          value={value()}
          onChange={onChange}
          fullWidth
        />

        <input
          type="submit"
          value="Save"
          style={{
            width: "20%",
            border: "none",
            color: "white",
            background:
              "linear-gradient(95.7deg, rgba(143, 38, 248, 0.2) 32.01%, rgba(143, 38, 248, 0) 95.2%), #208D8B",
            height: "36px",
            "border-radius": "24px",
          }}
        />
      </Box>
    </div>
  );
};

export default Form;
