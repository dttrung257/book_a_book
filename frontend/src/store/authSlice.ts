import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import Cookies from "js-cookie";
import { login } from "../actions/authActions";
import { LoginInfo } from "../models";

interface AuthState extends LoginInfo {
  isLoggedIn: boolean;
}

const initialState: AuthState = {
  accessToken: "",
  isLoggedIn: false,
  user: {
    firstName: "",
    lastName: "",
    role: [],
    avatar: "",
  },
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    storeInfo(state, action: PayloadAction<LoginInfo>) {
      state.isLoggedIn = true;
      state.accessToken = action.payload.accessToken;
      state.user = action.payload.user;

      Cookies.set("user", JSON.stringify(state.user));
      Cookies.set("token", state.accessToken);
    },
    logout(state) {
      state.isLoggedIn = false;
      state.accessToken = "";
      state.user = { firstName: "", lastName: "", role: [], avatar: "" };

      Cookies.remove("token");
      Cookies.remove("user");
    },
  },
  extraReducers: (builder) => {
    builder.addCase(login.fulfilled, (state, action) => {
      state.isLoggedIn = true;
      state.accessToken = action.payload.accessToken;
      state.user = action.payload.user;

      Cookies.set("user", JSON.stringify(state.user));
      Cookies.set("token", state.accessToken);
    });
    builder.addCase(login.rejected, (state, action) => {
      throw action.payload;
    });
  },
});

export const authActions = authSlice.actions;

export default authSlice.reducer;
