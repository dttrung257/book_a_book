import { ChangeEvent, Fragment, useContext, useEffect, useState } from "react";
import { FiSearch, FiUser, FiShoppingCart } from "react-icons/fi";
import { VscTriangleDown } from "react-icons/vsc";
import "./index.css";
import { Avatar, Badge, InputAdornment, TextField } from "@material-ui/core";
import { useAppSelector } from "../../store/hook";
import { Link } from "react-router-dom";

const Header = () => {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
  const { user, accessToken } = useAppSelector((state) => state.auth);
  const [searchKey, setSearchKey] = useState("");

  const handleSearch = () => {
    //redux
  };

  const onChangeSearchBox = (event: ChangeEvent<HTMLInputElement>) => {
    console.log(event.currentTarget.value);
    setSearchKey(event.currentTarget.value.trim());
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
            <a>Home</a>
          </div>
          <div className="navAddr">
            <a>
              Collections <VscTriangleDown />
            </a>
          </div>

          <div className="navAddr">
            <a>Blogs</a>
          </div>
          <div className="navAddr">
            <a>About us</a>
          </div>
        </div>
      </div>
      <div className="search">
        <input id="searchBar" placeholder="Search book..." />
        <FiSearch
          color="008B8B"
          onClick={handleSearch}
          id="searchIcon"
        ></FiSearch>
      </div>
      <div className="account">
        {isLoggedIn ? (
          <Fragment>
            <Avatar src={user.avatar} style={{ maxWidth: 25, maxHeight: 25 }} />
            <span>{`${user.firstName} ${user.lastName}`}</span>
            <Badge overlap="rectangular" badgeContent={4} color="error">
              <FiShoppingCart fontSize={20} />
            </Badge>
          </Fragment>
        ) : (
          <Fragment>
            <FiUser style={{ margin: "0 20px" }} fontSize={20} />
            <a>Log in</a>
            <svg height="30" width="10">
              <line
                x1="0"
                y1="0"
                x2="0"
                y2="30"
                style={{ stroke: "#999999", strokeWidth: 3 }}
              />
            </svg>
            <a>Sign up</a>
          </Fragment>
        )}
      </div>
    </div>
  );
};

export default Header;
