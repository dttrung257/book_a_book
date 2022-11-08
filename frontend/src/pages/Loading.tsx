import React from "react";
import styled from "styled-components";
import { AiOutlineLoading3Quarters } from "react-icons/ai";

const Container = styled.div`
  background-color: rgba(255, 255, 255, 0.5);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
`;

const Icon = styled.div`
  @keyframes rotate {
    from {
      transform: rotate(0);
    }
    to {
      transform: rotate(360deg);
    }
  }

  position: absolute;
  top: 50%;
  left: 50%;
  transform: translateX(-50%) translateY(-50%);
  animation: rotate 2s infinite linear;
`;

const Loading = ({ isSending }: { isSending: boolean }) => {
  return (
    <div>
      {isSending ? (
        <Container>
          <Icon>
            <AiOutlineLoading3Quarters size={30} />
          </Icon>
        </Container>
      ) : null}
    </div>
  );
};

export default Loading;
