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
export const Subject = [
  "FANTASY",
  "LIFESTYLE",
  "BUSINESS",
  "EDUCATION",
  "ROMANCE",
  "TECHNOLOGY",
  "SCIENCE",
  "DETECTIVE",
  "LITERARY",
  "COMIC",
];
export const priceRanges = [
  "Under $5",
  "$5 - $10",
  "$10 - $25",
  "$25 - $50",
  "Over $50",
];
export interface BookInfoBrief {
  id: number;
  name: string;
  image: string;
  author: string;
  sellingPrice: number;
  rating?: number;
}

export interface Book {
  id: number;
  name: string;
  image: string;
  category: Category;
  author: string;
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

export interface BookAddInfo {
  name: string;
  image: string;
  category: Category;
  author: string;
  width?: number | string;
  height?: number | string;
  isbn?: string | string;
  publisher?: string;
  numberOfPages?: number | string;
  yearOfPublication?: string;
  buyPrice: string;
  sellingPrice: string;
  description: string;
  quantityInStock: number | string;
}
