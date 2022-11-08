import React, { useState } from "react";
import { Link, Navigate, Outlet } from "react-router-dom";
import styled from "styled-components";
import { useAppSelector } from "../../store/hook";
import axios from "../../apis/axiosInstance";

const Wrapper = styled.div`
  background-color: #f8f8f8;
  min-height: 100vh;
  overflow: auto;
  position: relative;
`;

const Nav = styled.nav`
  height: 56px;
  padding: 0px 40px;
  border-bottom: 1px solid #ddd;
  background-color: white;
`;

const Container = styled.div`
  width: 600px;
  margin: 200px auto;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: white;

  @media screen and (max-width: 767px) {
    width: 460px;
  }

  @media screen and (max-width: 479px) {
    width: 100%;
    margin-top: 100px;
  }
`;

const Footer = styled.div`
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

const Layout = () => {
  const [email, setEmail] = useState<string>("");
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);

  if (isLoggedIn) return <Navigate to="/" />;

  const sendForgetPassword = async () => {
    return await axios.get(`/user/forgot_password/${email}`);
  };

  return (
    <Wrapper>
      <Nav className="d-flex align-items-center">
        <h1 className="m-0">
          <Link to="/" className="text-decoration-none text-dark">
            <i style={{ color: "var(--primary-color)", fontWeight: 500 }}>
              Book
            </i>
            a<i>Book</i>
          </Link>
        </h1>
      </Nav>

      <Container className="p-3">
        <Outlet context={{ sendForgetPassword, email, setEmail }} />
      </Container>

      <Footer className="d-flex flex-wrap justify-content-center align-items-center">
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
      </Footer>
    </Wrapper>
  );
};

export default Layout;
