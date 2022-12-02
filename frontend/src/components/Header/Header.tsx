import { ChangeEvent, Fragment, useState } from "react";
import { FiSearch, FiUser, FiShoppingCart } from "react-icons/fi";
import { VscTriangleDown } from "react-icons/vsc";
import "./index.css";
import { Avatar, Badge } from "@mui/material";
import { useAppSelector, useAppDispatch } from "../../store/hook";
import { Link, useNavigate } from "react-router-dom";
import { searchActions } from "../../store/searchSlice";

const Header = () => {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
  const { user, accessToken } = useAppSelector((state) => state.auth);
  const [searchKey, setSearchKey] = useState("");
  const [name, setName] = useState("");
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const handleSearch = () => { 
    //redux
    dispatch(searchActions.setNameSearch({name}));
    navigate("/books");
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
        <input id="searchBar" placeholder="Search book..." value={name} onChange={e => setName(e.target.value)}onChange={onChangeSearchBox}/>
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
            <FiUser style={{ marginRight: "10px" }} fontSize={20} />
            <a>Log in</a>
            <svg height="30" width="30">
              <line
                x1="15"
                y1="5"
                x2="15"
                y2="25"
                style={{ stroke: "#999999", strokeWidth: 2 }}
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
