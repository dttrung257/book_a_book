import styled from "styled-components";
import { Zoom } from "@mui/material";
import { BsCheck2Circle } from "react-icons/bs";
import { useEffect, useState } from "react";

const Container = styled.div`
  background-color: rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
`;

const PopUp = styled.div`
  position: absolute;
  width: 400px;
  height: 140px;
  text-align: center;
  background-color: rgba(255, 255, 255, 1);
  top: 35%;
  left: 40%;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 5px;
`;

const AlertSuccess = ({
  content,
  setIsSending,
}: {
  content: string;
  setIsSending: () => void;
}) => {
  const [mount, setMount] = useState<boolean>(true);
  useEffect(() => {
    document.body.style.overflow = "hidden";
    let timer = setTimeout(() => {
      setMount(false);
      document.body.style.overflow = "auto";
      setIsSending();
    }, 3000);
    return () => clearTimeout(timer);
  }, []);
  return (
    <div>
      <Container>
        <Zoom in={mount}>
          <PopUp>
            <div>
              <p style={{ fontSize: 24 }}>{content}</p>
              <BsCheck2Circle fontSize={40} color="green" />
            </div>
          </PopUp>
        </Zoom>
      </Container>
    </div>
  );
};

export default AlertSuccess;
