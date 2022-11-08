import axios from "axios";

export const isAxiosError = axios.isAxiosError;

//TODO: Change url, add authorization

export default axios.create({
  baseURL: "//localhost:9090/api",
  headers: {
    "Content-Type": "application/json",
  },
});
