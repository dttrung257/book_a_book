import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import emailVerifyReducer from "./emailVerifySlice";
import searchReducer from "./searchSlice";
const store = configureStore({
  reducer: {
    auth: authReducer,
    emailVerify: emailVerifyReducer,
    search: searchReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;

export default store;
