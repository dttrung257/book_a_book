import { CommentCore } from "../models";
import axiosInstance from "./axiosInstance";

export const getUserComments = async (id: number, options: Object) => {
  const response = await axiosInstance.get(
    `comments/user_comment?book_id=${id}`,
    options
  );
  return response.data;
};

export const getOtherComments = async (id: number, page: number, options: Object) => {
  const response = await axiosInstance.get(`comments/other_comment?book_id=${id}&page=${page}`, options);
  return response.data;
};

export const getAllComments = async (id: number, page: number) => {
  const response = await axiosInstance.get(`comments?book_id=${id}&page=${page}`);
  return response.data;
};

export const addComment = async (cmt : CommentCore, options: Object) => {
  const response = await axiosInstance.post(`comments`, cmt, options);
  return response.data;
};

export const updateComment = async (cmt : CommentCore, options: Object) => {
  const response = await axiosInstance.put(`comments`, cmt, options);
  return response.data;
};

export const deleteComment = async (bookId: number, options: Object) => {
  const response = await axiosInstance.delete(`comments?book_id=${bookId}`, options);
  return response.data;
};