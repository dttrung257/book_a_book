import { ChangeEvent, Fragment, useState } from "react";
import { FiSearch, FiUser, FiShoppingCart } from "react-icons/fi";
import { VscTriangleDown } from "react-icons/vsc";
import "./index.css";
import {
  Avatar,
  Badge,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
} from "@mui/material";
import { useAppDispatch, useAppSelector } from "../../store/hook";
import { Link, useNavigate } from "react-router-dom";
import { authActions } from "../../store/authSlice";

const Header = () => {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
  const user = useAppSelector((state) => state.auth.user);
  const totalQuantity = useAppSelector((state) => state.cart.totalQuantity);
  const navigate = useNavigate();
  const [searchKey, setSearchKey] = useState("");
  const dispatch = useAppDispatch();
  console.log(user);
  const handleSearch = () => {
    //redux
  };

  const onChangeSearchBox = (event: ChangeEvent<HTMLInputElement>) => {
    console.log(event.currentTarget.value);
    setSearchKey(event.currentTarget.value.trim());
  };

  const onLogout = () => {
    dispatch(authActions.logout());
  };

  return (
    <div className="header">
      <div id="headerLeft">
        <div className="logo">
          <Link to="/">
            <p>
              <span>
                <i>Book</i>
              </span>
              a<i>Book</i>
            </p>
          </Link>
        </div>
        <div className="nav">
          <div className="navAddr">
            <Link to="/">Home</Link>
          </div>
          <div className="navAddr">
            <Link to="/books">
              Collections
              <VscTriangleDown />
            </Link>
          </div>

          <div className="navAddr">
            <Link to="/blogs">Blogs</Link>
          </div>
          <div className="navAddr">
            <Link to="/about-us">About us</Link>
          </div>
        </div>
      </div>
      <div className="search">
        <input
          id="searchBar"
          placeholder="Search book..."
          onChange={onChangeSearchBox}
          value={searchKey}
        />
        <FiSearch
          color="008B8B"
          onClick={handleSearch}
          id="searchIcon"
        ></FiSearch>
      </div>
      <div className="account">
        {isLoggedIn ? (
          <Fragment>
            <div className="dropdown">
              <div
                style={{
                  display: "flex",
                }}
                className="dropbtn"
              >
                <Avatar
                  src={user.avatar}
                  style={{ maxWidth: 25, maxHeight: 25 }}
                />
                &nbsp;&nbsp;
                <span>{`${user.firstName} ${user.lastName}`}</span>
              </div>
              <div className="dropdown-content">
                <List>
                  <ListItem disablePadding>
                    <ListItemButton
                      style={{ padding: "3px 30px 3px 15px" }}
                      component="a"
                      href="/account"
                    >
                      <ListItemText
                        primaryTypographyProps={{
                          fontSize: 14,
                          fontWeight: 500,
                        }}
                        primary="My Account"
                      />
                    </ListItemButton>
                  </ListItem>
                  <ListItem disablePadding>
                    <ListItemButton
                      style={{ padding: "3px 30px 3px 15px" }}
                      component="a"
                      href="#simple-list"
                    >
                      <ListItemText
                        primaryTypographyProps={{
                          fontSize: 14,
                          fontWeight: 500,
                        }}
                        primary="My Purchase"
                      />
                    </ListItemButton>
                  </ListItem>
                  <ListItem disablePadding>
                    <ListItemButton
                      style={{ padding: "3px 30px 3px 15px" }}
                      component="a"
                      href=""
                      onClick={onLogout}
                    >
                      <ListItemText
                        primaryTypographyProps={{
                          fontSize: 14,
                          fontWeight: 500,
                        }}
                        primary="Log out"
                      />
                    </ListItemButton>
                  </ListItem>
                </List>
              </div>
            </div>

            <Badge
              overlap="rectangular"
              badgeContent={totalQuantity}
              color="error"
              onClick={() => navigate("/cart")}
            >
              <FiShoppingCart fontSize={20} />
            </Badge>
          </Fragment>
        ) : (
          <Fragment>
            <FiUser style={{ marginRight: "10px" }} fontSize={20} />
            <Link to="/login">Log in</Link>
            <svg height="30" width="30">
              <line
                x1="15"
                y1="5"
                x2="15"
                y2="25"
                style={{ stroke: "#999999", strokeWidth: 2 }}
              />
            </svg>
            <Link to="/signup">Sign up</Link>
          </Fragment>
        )}
      </div>
    </div>
  );
};

export default Header;
