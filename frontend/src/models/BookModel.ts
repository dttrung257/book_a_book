export enum Category {
  FANTASY = "FANTASY",
  LIFESTYLE = "LIFESTYLE",
  BUSINESS = "BUSINESS",
  EDUCATION = "EDUCATION",
  ROMANCE = "ROMANCE",
  TECHNOLOGY = "TECHNOLOGY",
  SCIENCE = "SCIENCE",
  DETECTIVE = "DETECTIVE",
  LITERARY = "LITERARY",
  COMIC = "COMIC",
}

export interface BookInfoBrief {
  id: number;
  name: string;
  image: string;
  author: string;
  sellingPrice: number;
}
export interface BookInfo {
  id: number;
  name: string;
  image: string;
  author: string;
  sellingPrice: number;
  rating: number;
}

export interface Book {
  id: number;
  name: string;
  image: string;
  category: Category;
  author: String;
  width?: number;
  height?: number;
  isbn?: string;
  publisher?: string;
  numberOfPages?: number;
  yearOfPublication?: number;
  buyPrice: number;
  sellingPrice: number;
  description: string;
  quantityInStock: number;
  availableQuantity: number;
  quantitySold: number;
  stopSelling: boolean;
  rating?: number;
}
