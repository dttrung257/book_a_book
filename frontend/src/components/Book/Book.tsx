import { useEffect, useState } from "react";
import { BsCartPlus } from "react-icons/bs";
import { getBookViaId } from "../../apis/book";
import { BookInfoBrief } from "../../models";
import "./index.css";

const Book = (props: { id: number }) => {
  const [info, setInfo] = useState<BookInfoBrief>({
    id: props.id,
    name: "",
    image: "",
    author: "",
    sellingPrice: 0,
  });

  useEffect(() => {
    getBookViaId(props.id).then((res) => {
      console.log(res);
      setInfo({
        id: props.id,
        name: res.name,
        image: res.image,
        author: res.author,
        sellingPrice: res.sellingPrice,
      });
    });
  }, []);

  const formatStr = (s: string) => {
    if (s.length < 20) return s;
    else {
      return s.slice(0, 18).concat("...");
    }
  };

  return (
    <div className="frame">
      <div>
        <img src={info.image} width={280} height={320} />
      </div>
      <div id="info">
        <div id="text">
          <h3>{formatStr(info.name)}</h3>
          <h4>{info.author}</h4>
          <p>{`${info.sellingPrice}$`}</p>
        </div>
        <div id="cart">
          <BsCartPlus fontSize={30} color="008b8b" />
        </div>
      </div>
    </div>
  );
};

export default Book;
