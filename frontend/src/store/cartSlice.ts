import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { toast } from "react-toastify";

interface Cart {
  items: { id: number; quantity: number }[];
  totalQuantity: number;
}

const initialState: Cart = {
  items: [],
  totalQuantity: 0,
};

const MAX_ITEMS = 10;

const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    storeInfo(
      state,
      action: PayloadAction<{ id: number; quantity: number }[]>
    ) {
      state.items = action.payload;

      state.totalQuantity = state.items.length;
    },
    addToCart(
      state,
      action: PayloadAction<{
        id: number;
        stopSelling: boolean;
        quantity?: number;
      }>
    ) {
      const { id, stopSelling, quantity } = action.payload;
      const addedQuantity = quantity || 1;

      if (stopSelling) {
        toast.error("This book is no longer available!");
        return;
      }

      const itemIndex = state.items.findIndex((item) => item.id === id);

      if (itemIndex >= 0) {
        state.items[itemIndex].quantity += addedQuantity;
        toast.success("Book has been successfully added");
      } else {
        if (state.items.length === MAX_ITEMS)
          toast.error("Cart has maximum items!");
        else {
          state.items.push({ id: id, quantity: addedQuantity });
          toast.success("Book has been successfully added");
          console.log(state);
        }
      }

      state.totalQuantity = state.items.length;

      localStorage.setItem("cartItems", JSON.stringify(state.items));
    },
    changeQuantity(
      state,
      action: PayloadAction<{ id: number; quantity: number }>
    ) {
      const itemIndex = state.items.findIndex(
        (item) => item.id === action.payload.id
      );

      state.items[itemIndex].quantity = action.payload.quantity;

      localStorage.setItem("cartItems", JSON.stringify(state.items));
    },
    removeCart(state, action: PayloadAction) {
      state.items = [];

      state.totalQuantity = 0;

      localStorage.setItem("cartItems", JSON.stringify(state.items));
    },
    removeCartItems(state, action: PayloadAction<{ ids: number[] }>) {
      state.items = state.items.filter(
        (item) => !action.payload.ids.includes(item.id)
      );

      state.totalQuantity = state.items.length;

      localStorage.setItem("cartItems", JSON.stringify(state.items));
    },
  },
});

export const cartActions = cartSlice.actions;

export default cartSlice.reducer;
