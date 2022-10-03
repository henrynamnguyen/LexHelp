import { Component } from "solid-js";
import styles from "../App.module.css";
import plus from "../assets/plus.png";

interface UploadAreaProps {
  onUpload: (f: File) => void;
}

const UploadArea: Component<UploadAreaProps> = (props) => {
  const onChange = (ev: Event) => {
    const target = ev.target as HTMLInputElement;
    const files = target.files;
    if (files && files[0]) {
      props.onUpload(files[0]);
    }
  };

  return (
    <div class={styles.Upload}>
      <label for="upload">
        <img src={plus} alt="" />
        <p class={styles.UploadText}>Upload PDF</p>
      </label>
      <input id="upload" type="file" onChange={onChange} />
    </div>
  );
};

export default UploadArea;
