import Container from "@suid/material/Container";
import Grid from "@suid/material/Grid";
import { Component } from "solid-js";
import { style } from "solid-js/web";
import styles from "../App.module.css";
import logo from "../assets/logo.svg";
import pdfIcon from "../assets/pdficon.png";
import Created from "./Created";
import UploadArea from "./UploadArea";
import Send from "./Send";

interface HomeProps {
  onUpload: (f: File) => void;
}

const Home: Component<HomeProps> = (props) => {
  return (
    <div class={styles.Home}>
      <Container maxWidth="xl">
        <div>
          <img src={logo} alt="" style={{ margin: "24px 0" }} />
        </div>
        <div class={styles.Header}>
          <div>
            <h1>Annotate Your PDFs</h1>
            <h3>
              Upload a PDF form and add a question corresponding to each field.
            </h3>
          </div>
          <img src={pdfIcon} alt="" />
        </div>
        <div class={styles.Lower}>
          <Grid container spacing={8} alignItems={"center"}>
            <Grid item sm={3} justifyContent={"center"}>
              <UploadArea onUpload={props.onUpload} />
            </Grid>
            <Grid item sm={3} justifyContent={"center"}>
              <Created num={0}/>
            </Grid>
            <Grid item sm={3} justifyContent={"center"}>
              <Created num={1} />
            </Grid>
            <Grid item sm={3} justifyContent={"center"}>
              <Created num={2} />
            </Grid>
          </Grid>
        </div>
      </Container>
    </div>
  );
};

export default Home;
