import { Component } from "solid-js";
import pencil from "../assets/pencil.png";

const Pencil: Component = () => {
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
      <img src={pencil} alt="" />
      Click on a highlighted
      <br></br>
      field to start annotating
    </div>
  );
};

export default Pencil;
