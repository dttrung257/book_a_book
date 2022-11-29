import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  name: "",
};

const searchSlice = createSlice({
  name: "filter",
  initialState,
  reducers: {
    setNameSearch(state, action) {
      state.name = action.payload.name;
    },
  },
});

export const searchActions = searchSlice.actions;

export default searchSlice.reducer;
