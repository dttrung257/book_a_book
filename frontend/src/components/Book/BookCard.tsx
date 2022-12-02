import "./index.css";
import { BookInfoBrief } from "../../models";
import { useNavigate } from "react-router-dom";
import { formatStr } from "../../utils";
const BookCard = (props: { book: BookInfoBrief }) => {
  const navigate = useNavigate();
  let path = `/product/${props.book.id}/${props.book.name
    .split(" ")
    .join("-")
    .toLowerCase()}`;
  const handleProductClick = () => {
    navigate(path);
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
          <div id="QuickAdd">Quick Add</div>
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
