import React from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";

const Container = styled.div`
  background-color: var(--primary-color);
  position: absolute;
  bottom: 0;
  width: 100%;
  min-height: 60px;

  & > div {
    padding: 10px 20px;
  }

  & a {
    color: white;
    text-decoration: none;
  }
`;

const Footer = () => {
  return (
    <Container className="d-flex flex-wrap justify-content-center align-items-center">
      <div>
        <Link to={"#"}>About</Link>
      </div>
      <div>
        <Link to={"#"}>Privacy</Link>
      </div>
      <div>
        <Link to={"#"}>Help & Support</Link>
      </div>
      <div>
        <Link to={"#"}>Contact</Link>
      </div>
      <div>
        <Link to={"#"}>@2022-BookaBook</Link>
      </div>
    </Container>
  );
};

export default Footer;
