import React, { useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import { Button } from "@mui/material";
import { HiOutlineTrash } from "react-icons/hi";
import { IoAdd, IoRemove } from "react-icons/io5";
import AppModal from "../../components/AppModal/AppModal";
import { Book } from "../../models";
import { cartActions } from "../../store/cartSlice";
import { useAppDispatch } from "../../store/hook";
import { formatStr } from "../../utils";
import style from "./Cart.module.css";

interface BookCart {
  book: Book;
  quantity: number;
  checked: boolean;
}

interface Props extends BookCart {
  booksInfo: BookCart[];
  setBooksInfo: React.Dispatch<BookCart[]>;
}

const CartItem = ({
  book,
  quantity,
  checked,
  booksInfo,
  setBooksInfo,
}: Props) => {
  const dispatch = useAppDispatch();
  const [removeModal, setRemoveModal] = useState<boolean>(false);

  const handleAmount = (id: number, newAmount: number) => {
    setBooksInfo(
      booksInfo.map((bookInfo) => {
        if (bookInfo.book.id === book.id) {
          return {
            ...bookInfo,
            quantity: newAmount,
          };
        }
        return bookInfo;
      })
    );
    dispatch(
      cartActions.changeQuantity({
        id: id,
        quantity: newAmount,
      })
    );
  };

  const increaseAmount = () => {
    if (quantity + 1 > book.availableQuantity) {
      toast.warning(
        `"${book.name}" now has only ${book.availableQuantity} left in stock`
      );
    }
    const newAmount =
      quantity + 1 <= book.availableQuantity ? quantity + 1 : quantity;
    handleAmount(book.id, newAmount);
  };

  const decreaseAmount = () => {
    const newAmount = quantity - 1 >= 0 ? quantity - 1 : quantity;
    if (newAmount === 0) {
      return setRemoveModal(true);
    }
    handleAmount(book.id, newAmount);
  };

  const handleChangeAmount = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.value === "") {
      handleAmount(book.id, 1);
      return;
    }
    let newAmount = event.target.valueAsNumber;
    if (newAmount > book.availableQuantity) {
      toast.warning(
        `"${book.name}" now has only ${book.availableQuantity} left in stock`
      );
      newAmount = book.availableQuantity;
    } else if (newAmount < 1) newAmount = 1;
    handleAmount(book.id, newAmount);
  };

  const handleDelete = () => {
    dispatch(cartActions.removeCartItems({ ids: [book.id] }));
    setBooksInfo(booksInfo.filter((bookInfo) => bookInfo.book.id !== book.id));
    setRemoveModal(false);
  };

  const handleChecked = () => {
    setBooksInfo(
      booksInfo.map((bookInfo) => {
        return bookInfo.book.id === book.id
          ? { ...bookInfo, checked: !checked }
          : bookInfo;
      })
    );
  };

  return (
    <div
      className={`${style.cartItem} ${style.row} ${style.box} mt-1 py-3 px-0`}
    >
      <div>
        <input
          className="form-check-input"
          type="checkbox"
          checked={checked}
          name=""
          id=""
          onChange={handleChecked}
        />
      </div>
      <div className={`${style.productInfo}`}>
        <div>
          <Link
            to={`/product/${book.id}/${book.name
              .split(" ")
              .join("-")
              .toLowerCase()}`}
          >
            <img src={book.image} alt="img" />
          </Link>
        </div>
        <div className="ps-2">
          <Link
            to={`/product/${book.id}/${book.name
              .split(" ")
              .join("-")
              .toLowerCase()}`}
            className={`${style.name}`}
          >
            <h5>{formatStr(book.name, 28)}</h5>
          </Link>
          <div className={`${style.author} text-muted`}>
            Author: {formatStr(book.author, 28)}
          </div>
          <div className={`${style.category} text-muted`}>
            Category:{" "}
            {formatStr(
              book.category.charAt(0).toUpperCase() +
                book.category.slice(1).toLowerCase(),
              28
            )}
          </div>
        </div>
      </div>
      <div>{book.sellingPrice}$</div>
      <div className={style.quantity}>
        <div style={{ display: "flex" }}>
          <div
            className={`${style.selectAmount}`}
            onClick={() => decreaseAmount()}
          >
            <IoRemove />
          </div>
          <input type="number" value={quantity} onChange={handleChangeAmount} />
          <div
            className={`${style.selectAmount}`}
            onClick={() => increaseAmount()}
          >
            <IoAdd />
          </div>
        </div>
      </div>
      <div>{(book.sellingPrice * quantity).toFixed(2)}$</div>
      <div>
        <HiOutlineTrash
          className={`${style.trashIcon}`}
          onClick={() => setRemoveModal(true)}
        />
      </div>
      <AppModal
        showModal={removeModal}
        setShowModal={setRemoveModal}
        title={"Remove product?"}
      >
        <div>
          <p>Do you want to delete the selected product?</p>
          <div className={style.btnGroup}>
            <Button variant="outlined" color="error" onClick={handleDelete}>
              Delete
            </Button>
            <Button variant="contained" onClick={() => setRemoveModal(false)}>
              Cancel
            </Button>
          </div>
        </div>
      </AppModal>
    </div>
  );
};

export default CartItem;
