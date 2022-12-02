export interface Comment {
  id: number;
  userId: number;
  bookId: number;
  fullName: string;
  avatar: string;
  bookName: string;
  star: number;
  content: string;
}

export interface CommentCore {
  bookId: number;
  star: number;
  content: string;
}
