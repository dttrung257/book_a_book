import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const emailVerifySlice = createSlice({
	name: "emailVerify",
	initialState: {
		email: "",
	},
	reducers: {
		setEmail(state, action: PayloadAction<{ email: string }>) {
			state.email = action.payload.email;
		},
		clearEmail(state) {
			state.email = "";
		},
	},
});

export const emailVerifyActions = emailVerifySlice.actions;

export default emailVerifySlice.reducer;
