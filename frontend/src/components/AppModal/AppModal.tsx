import React, { ReactNode, useRef, useEffect } from "react";
import styled from "styled-components";
import { CSSTransition } from "react-transition-group";
import { Modal } from "react-bootstrap";
import { GrFormClose } from "react-icons/gr";
import style from "./AppModal.module.css";

const Container = styled.div`
  .modal-enter {
    opacity: 0;
  }
  .modal-enter-active {
    opacity: 1;
    transition: all 300ms;
  }
  .modal-exit {
    opacity: 1;
  }
  .modal-exit-active {
    opacity: 0;
    transition: all 300ms;
  }
`;

interface Props {
  showModal: boolean;
  setShowModal: (show: boolean) => void | React.Dispatch<boolean>;
  title: string;
  children: ReactNode;
}

const AppModal = ({ showModal, setShowModal, children, title }: Props) => {
  const modalRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const onKeyPress = (e: KeyboardEvent) => {
      if (e.key === "Escape") {
        setShowModal(false);
      }
    };

    document.body.addEventListener("keydown", onKeyPress, { capture: true });

    return () => {
      document.body.removeEventListener("keydown", onKeyPress, {
        capture: true,
      });
    };
  }, []);

  return (
    <Container>
      <CSSTransition
        in={showModal}
        timeout={300}
        nodeRef={modalRef}
        classNames="modal"
        unmountOnExit
        mountOnEnter
      >
        {(state) => (
          <div ref={modalRef}>
            <div className={style.modalContainerOver}>
              <div className={style.modalContainer}>
                <Modal.Dialog className={style.modal}>
                  <Modal.Header>
                    <Modal.Title>{title}</Modal.Title>
                    <GrFormClose
                      size={30}
                      style={{ cursor: "pointer" }}
                      onClick={() => setShowModal(false)}
                    />
                  </Modal.Header>

                  <Modal.Body className={` my-2`}>{children}</Modal.Body>
                </Modal.Dialog>
              </div>
            </div>
          </div>
        )}
      </CSSTransition>
    </Container>
  );
};

export default AppModal;
