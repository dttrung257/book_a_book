import { FilterSearch } from "../models/Filter";
import axiosInstance from "./axiosInstance";

export const getBookViaId = async (id: number) => {
  const response = await axiosInstance.get(`books/${id}`);
  return response.data;
};

export const getAllBook = async () => {
  try {
    const response = await axiosInstance.get("/books");
    return response.data.content;
  } catch (error) {
    console.log(error);
  }
};
//param page size
export const getBestSeller = async (filter: FilterSearch) => {
  let query = "";
  if (filter.size !== undefined) {
    query = `?size=${filter.size}`;
    if (filter.page !== undefined) query = query.concat(`&page=${filter.page}`);
  }
  if (filter.page !== undefined) query = `?page=${filter.page}`;
  const response = await axiosInstance.get(`books${query}`);
  return response.data;
};

export const getBooksOfCategory = async (filter: FilterSearch) => {
  let query = "";
  if (filter.category !== undefined) {
    query = `?category=${filter.category}`;
  }
  if (filter.size !== undefined) {
    query = query.concat(`&size=${filter.size}`);
  }
  if (filter.page !== undefined) query = query.concat(`&page=${filter.page}`);
  // console.log(query)
  const response = await axiosInstance.get(`books${query}`);
  return response.data;
};
