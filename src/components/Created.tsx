import { Component } from "solid-js";
import styles from "../App.module.css";
import preview from "../assets/preview1.png";
import preview2 from "../assets/preview2.png";
import preview3 from "../assets/preview3.png";

const Created: Component<{ num?: number }> = (props) => {
  let imgPreview = preview;
  let formName = "Onboarding.pdf";

  if (props.num == 1) {
    imgPreview = preview2;
    formName = "NDA_agreement.pdf";
  } else if (props.num == 2) {
    imgPreview = preview3;
    formName = "TD1_form.pdf";
  }

  return (
    <div class={styles.Created}>
      <img src={imgPreview} alt=""></img>
      <div>{formName}</div>
    </div>
  );
};

export default Created;
