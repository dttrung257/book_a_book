import { ChangeEvent, Fragment, useContext, useEffect, useState } from "react";
import { FiSearch, FiUser, FiShoppingCart } from "react-icons/fi";
import { VscTriangleDown } from "react-icons/vsc";
import "./index.css";
import { Avatar, Badge, InputAdornment, TextField } from "@material-ui/core";
import { useAppSelector } from "../../store/hook";

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
      <div className="logo">
        <p>
          <span>
            <i>Book</i>
          </span>
          a<i>Book</i>
        </p>
      </div>
      <div className="nav">
        <div id="home">
          <a>Home</a>
        </div>
        <div>
          <a>
            Collections <VscTriangleDown />
          </a>
        </div>

        <div>
          <a>Blogs</a>
        </div>
        <div>
          <a>About us</a>
        </div>
      </div>
      <div className="search">
        <TextField
          id="outlined-basic"
          variant="outlined"
          size="small"
          value={searchKey}
          onChange={onChangeSearchBox}
          placeholder="Search product..."
          fullWidth={true}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <FiSearch
                  color="008B8B"
                  onClick={handleSearch}
                  fontSize={24}
                  style={{ marginBottom: "3px" }}
                ></FiSearch>
              </InputAdornment>
            ),
          }}
        />
      </div>
      <div className="account">
        {isLoggedIn ? (
          <Fragment>
            <Avatar
              src={user.avatar}
              style={{ maxWidth: 30, maxHeight: 30, margin: "0 20px" }}
            />
            <span>{`${user.firstName} ${user.lastName}`}</span>
            <Badge badgeContent={4} color="error">
              <FiShoppingCart fontSize={26} />
            </Badge>
          </Fragment>
        ) : (
          <Fragment>
            <FiUser style={{ margin: "0 20px" }} fontSize={26} />
            <a>Log in</a>
            <svg height="30" width="10">
              <line
                x1="0"
                y1="0"
                x2="0"
                y2="30"
                style={{ stroke: "#999999", strokeWidth: 4 }}
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
