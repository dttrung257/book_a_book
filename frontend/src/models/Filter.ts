export interface FilterSearch {
  //num of items in 1 page
  size?: number;
  page?: number;
  name?: string;
  category?: string;
  rating?: number;
  from?: number;
  to?: number;
  best_selling?: boolean;
}

export interface BookFilter {
  size?: number;
  page: number;
  category?: string;
  rating: number;
  from: number;
  to: number;
}
