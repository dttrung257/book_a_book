import "./index.css";
import { BookInfoBrief } from "../../models";
import { useNavigate, useLocation } from "react-router-dom";
import { formatStr } from "../../utils";
import { useAppDispatch, useAppSelector } from "../../store/hook";
import { cartActions } from "../../store/cartSlice";
import { useState } from "react";

const BookCard = (props: { book: BookInfoBrief }) => {
  const navigate = useNavigate();
  const { isLoggedIn } = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();
  const location = useLocation();
  const [checked, setChecked] = useState(false);

  let path = `/product/${props.book.id}/${props.book.name
    .split(" ")
    .join("-")
    .toLowerCase()}`;
  const handleProductClick = () => {
    navigate(path);
  };
  const handleAddToCart = () => {
    if (!isLoggedIn) {
      return navigate("/login", {
        replace: true,
        state: { from: location },
      });
    }
    console.log("here");
    dispatch(
      cartActions.addToCart({
        id: props.book.id,
        stopSelling: false,
        quantity: 1,
      })
    );
    setChecked(true);
  };
  return (
    <div className="CardFrame">
      <div id="CardImg">
        <img
          src={props.book.image}
          width={160}
          height={200}
          alt="error"
          onClick={handleProductClick}
        />
      </div>
      <div id="CardInfor">
        <div id="AddContainer">
          <div id="QuickAdd" onClick={handleAddToCart}>
            Quick Add
          </div>
        </div>
        <div id="CardText">
          <h3 id="CardName" onClick={handleProductClick}>
            {formatStr(props.book.name, 16)}
          </h3>
          <p style={{ color: "#008b8b" }}>{formatStr(props.book.author, 15)}</p>
          <p style={{ fontSize: "10" }}>{`${props.book.sellingPrice}$`}</p>
        </div>
      </div>
    </div>
  );
};

export default BookCard;
