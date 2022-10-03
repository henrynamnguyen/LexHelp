import { Component } from "solid-js";
import styles from "../App.module.css";
import Box from "@suid/material/Box";
import Modal from "@suid/material/Modal";
import IconButton from "@suid/material/IconButton";
import { createSignal } from "solid-js";
import copyIcon from "../assets/copy.svg";
import orLine from "../assets/orLine.svg";
import emailIcon from "../assets/emailIcon.svg";
import exitIcon from "../assets/exit.svg";

interface SendProps {
  open: boolean;
  onClose?: () => void;
}

const Send: Component<SendProps> = (props) => {
  const link = "https://linktoPDF/HDsdjDI3";
  const code = "817394";

  return (
    <div>
      <Modal
        open={props.open}
        onClose={props.onClose}
        aria-labelledby="Send PDF"
        aria-describedby="To share your PDF, send the code or link, or send an email!"
      >
        <Box
          sx={{
            background:
              "linear-gradient(304.45deg, rgba(44, 255, 129, 0.2) 1.9%, rgba(44, 255, 129, 0) 44.4%), linear-gradient(109.61deg, rgba(143, 38, 248, 0.2) 17.42%, rgba(143, 38, 248, 0) 66.85%), #186F8B;",
            position: "absolute",
            top: "50%",
            left: "50%",
            borderRadius: "16px",
            transform: "translate(-50%, -50%)",
            boxShadow: "24px",
            display: "flex",
            flexDirection: "column",
            p: 10,
          }}
        >
          <p class={styles.SendTitle}>
            Send PDF
            <span style="float: right">
              <IconButton onClick={props.onClose}>
                <img src={exitIcon}></img>
              </IconButton>
            </span>
          </p>
          <p class={styles.SendLabel}>Send a link</p>
          <div class={styles.SendBox}>
            <p class={styles.SendBoxText}>
              {link}
              <button class={styles.SendBoxCopyButton}>
                <img src={copyIcon} alt="copy icon" />
              </button>
            </p>
          </div>
          <p class={styles.SendLabel}>Send a code</p>
          <div class={styles.SendBox}>
            <p class={styles.SendBoxText}>
              {code}
              <button class={styles.SendBoxCopyButton}>
                <img src={copyIcon} alt="copy icon" />
              </button>
            </p>
          </div>
          <div class={styles.OrLine}>
            <img width={550} src={orLine} alt="or" />
          </div>
          <div class={styles.Center}>
            <button type="button" class={styles.EmailButton}>
              <div class={styles.Left}>
                <img src={emailIcon} alt="email icon" />
              </div>
              <span>Send an email</span>
            </button>
            <p class={styles.SkipText}>Skip for now</p>
          </div>
        </Box>
      </Modal>
    </div>
  );
};

export default Send;
