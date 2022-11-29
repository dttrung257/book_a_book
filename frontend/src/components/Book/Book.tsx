import { TooltipProps, Tooltip } from "@mui/material";
import { styled } from "@mui/material";
import { useState } from "react";
import { BsCartPlus } from "react-icons/bs";
import { BookInfoBrief } from "../../models";
import "./index.css";
import { Link } from "react-router-dom";
import { formatStr } from "../../utils";

const LightTooltip = styled(({ ...props }: TooltipProps) => (
  <Tooltip {...props} />
))(() => ({
  [`& .MuiTooltip-tooltip`]: {
    fontSize: 12,
  },
}));

const Book = (props: { book: BookInfoBrief }) => {
  const [info, setInfo] = useState<BookInfoBrief>({
    id: props.book.id,
    name: props.book.name,
    image: props.book.image,
    author: props.book.author,
    sellingPrice: props.book.sellingPrice,
  });

  let path = `/product/${info.id}/${info.name
    .split(" ")
    .join("-")
    .toLowerCase()}`;

  return (
    <div className="frame">
      <Link to={path}>
        <div>
          <img src={info.image} />
        </div>
      </Link>
      <div id="info">
        <div id="text">
          <Link to={path}>
            <LightTooltip title={info.name}>
              <p id="bookName">{formatStr(info.name, 20)}</p>
            </LightTooltip>
          </Link>
          <p id="author">{formatStr(info.author, 25)}</p>
          <p id="price">{`${info.sellingPrice}$`}</p>
        </div>
        <div id="cart">
          <BsCartPlus fontSize={24} color="008b8b" />
        </div>
      </div>
    </div>
  );
};

export default Book;
