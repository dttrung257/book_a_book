import axios from "axios";

//TODO: Change url, add authorization

export default axios.create({
	baseURL: "https://identitytoolkit.googleapis.com/v1",
	headers: {
		"Content-Type": "application/json",
	},
});
