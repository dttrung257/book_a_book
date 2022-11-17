import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { HiUserGroup } from "react-icons/hi";
import { MdLibraryBooks } from "react-icons/md";
import { RiFileList3Fill } from "react-icons/ri";
import style from "./DashBoardLayout.module.css";
import { Avatar } from "@material-ui/core";
import { useAppSelector } from "../../store/hook";
import { NavLink } from "react-router-dom";

const Header = () => {
  const user = useAppSelector((state) => state.auth.user);

  return (
    <header>
      <div id={style.headerLeft}>
        <div className={style.logo}>
          <NavLink to="/">
            <p>
              <span>
                <i>Book</i>
              </span>
              a<i>Book</i>
            </p>
          </NavLink>
        </div>
        <h3 className={style.title}>Management</h3>
      </div>

      <div className={style.account}>
        <>
          <Avatar src={user.avatar} style={{ maxWidth: 25, maxHeight: 25 }} />
          <span>{`${user.firstName} ${user.lastName}`}</span>
        </>
      </div>
    </header>
  );
};

const Footer = () => {
  return (
    <footer id={style.copyright} className="text-center">
      <p>@2022- Book a book All Rights Reserved</p>
    </footer>
  );
};

const Navbar = () => {
  return (
    <nav>
      <div>
        <NavLink
          to={"users"}
          className={({ isActive }) => (isActive ? style.isActive : "")}
        >
          <HiUserGroup />
          Users
        </NavLink>
      </div>
      <div>
        <NavLink
          to={"books"}
          className={({ isActive }) => (isActive ? style.isActive : "")}
        >
          <MdLibraryBooks />
          Books
        </NavLink>
      </div>
      <div>
        <NavLink
          to={"orders"}
          className={({ isActive }) => (isActive ? style.isActive : "")}
        >
          <RiFileList3Fill />
          Orders
        </NavLink>
      </div>
    </nav>
  );
};

const DashBoardLayout = () => {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
  const user = useAppSelector((state) => state.auth.user);

  if (!isLoggedIn || user.authority !== "ADMIN") return <Navigate to="/" />;

  return (
    <div>
      <Header />
      <div className={`${style.container} d-flex flex-row`}>
        <Navbar />
        <div className={"p-3 bg-transparent flex-grow-1"}>
          <Outlet />
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default DashBoardLayout;
