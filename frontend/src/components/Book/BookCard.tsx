import { BsCartPlus } from "react-icons/bs";
import "./index.css";

const BookCard = (props: {
  id: number;
  name: string;
  image: string;
  author: string;
  sellingPrice: number;
  rating: number;
}) => {
  const formatStr = (s: string) => {
    if (s.length < 20) return s;
    else {
      return s.slice(0, 18).concat("...");
    }
  };

  return (
    <div className="frame">
      <div>
        <img src={props.image} width={140} height={160} />
      </div>
      <div id="info">
        <div id="text">
          <h3>{formatStr(props.name)}</h3>
          <h4>{props.author}</h4>
          <p>{`${props.sellingPrice}$`}</p>
        </div>
        <div id="cart">
          <BsCartPlus fontSize={30} color="008b8b" />
        </div>
      </div>
    </div>
  );
};

export default BookCard;
