import { useEffect, useState } from "react";
import { BsCartPlus } from "react-icons/bs";
import { getBookViaId } from "../../apis/book";
import { BookInfoBrief } from "../../models";
import "./index.css";

interface Props {
  id?: number;
  bestSeller?: BookInfoBrief;
}

const Book = (props: Props) => {
  const [info, setInfo] = useState<BookInfoBrief>({
    id: 0,
    name: "",
    image: "",
    author: "",
    sellingPrice: 0,
  });

  useEffect(() => {
    if (props.id !== undefined) {
      getBookViaId(props.id).then((res) => {
        setInfo({
          id: props.id!,
          name: res.name,
          image: res.image,
          author: res.author,
          sellingPrice: res.sellingPrice,
        });
      });
    }
    if (props.bestSeller !== undefined) {
      setInfo({
        id: props.bestSeller.id,
        name: props.bestSeller.name,
        image: props.bestSeller.image,
        author: props.bestSeller.author,
        sellingPrice: props.bestSeller.sellingPrice,
      });
    }
  }, []);

  const formatStr = (s: string, n: number) => {
    if (s.length < n) return s;
    else {
      return s.slice(0, n - 2).concat("...");
    }
  };

  return (
    <div className="frame">
      <div>
        <img src={info.image} />
      </div>
      <div id="info">
        <div id="text">
          <p>{formatStr(info.name, 20)}</p>
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
