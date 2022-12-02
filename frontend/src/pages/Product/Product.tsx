import { Button } from "@mui/material";
import { ChangeEvent, useEffect, useState } from "react";
import { AiFillThunderbolt } from "react-icons/ai";
import { FaStar } from "react-icons/fa";
import { IoAdd, IoRemove } from "react-icons/io5";
import { MdCollectionsBookmark } from "react-icons/md";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { getBooksOfCategory, getBookViaId } from "../../apis/book";
import BookCarousel from "../../components/BookCarousel/BookCarousel";
import Span from "../../components/Span";
import { Book, BookInfoBrief, Category } from "../../models";
import "./index.css";
import BookDetail from "../../components/BookDetail/BookDetail";
import { formatStr } from "../../utils";
import Comment from "../../components/Comment/Comment";
import { useAppDispatch, useAppSelector } from "../../store/hook";
import { cartActions } from "../../store/cartSlice";
import AlertSuccess from "../../components/AlertSuccess";

const Product = () => {
  const params = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const { isLoggedIn } = useAppSelector((state) => state.auth);
  const [recommend, setRecommend] = useState<BookInfoBrief[]>([]);
  const [amount, setAmount] = useState<number>(1);
  const [isSending, setIsSending] = useState(false);
  const [info, setInfo] = useState<Book>({
    id: 0,
    name: "",
    image: "",
    category: Category.BUSINESS,
    author: "",
    buyPrice: 0,
    sellingPrice: 0,
    description: "",
    quantityInStock: 0,
    availableQuantity: 0,
    quantitySold: 0,
    stopSelling: true,
  });

  useEffect(() => {
    getBookViaId(params.id as unknown as number).then((res) => {
      setAmount(1);
      setInfo(res);
      getBooksOfCategory({ category: res.category, size: 10, rating: 0 })
        .then((res) => {
          setRecommend(res.content as BookInfoBrief[]);
        })
        .catch((err) => {
          console.log(err);
        });
    });
    window.scrollTo(0, 0);
  }, [params.id]);

  const addAmount = (c: boolean) => {
    setAmount(
      c
        ? amount + 1 <= info.availableQuantity
          ? amount + 1
          : amount
        : amount - 1 >= 1
        ? amount - 1
        : amount
    );
  };

  //TODO: check amount can add (already have in cart or not)
  const handleChangeAmount = (event: ChangeEvent<HTMLInputElement>) => {
    if (event.target.value === "") {
      setAmount(1);
      return;
    }
    let tmp = event.target.valueAsNumber;
    if (tmp > info.availableQuantity) tmp = info.availableQuantity;
    else if (tmp < 1) tmp = 1;
    setAmount(tmp);
  };

  const handleAddToCart = () => {
    if (!isLoggedIn) {
      return navigate("/login", {
        replace: true,
        state: { from: location },
      });
    }
    dispatch(
      cartActions.addToCart({
        id: info.id,
        stopSelling: info.stopSelling,
        quantity: amount,
      })
    );
    setIsSending(true);
  };

  return (
    <div id="productPage">
      {isSending ? (
        <AlertSuccess
          setIsSending={() => setIsSending(false)}
          content="Successfully added to cart"
        />
      ) : (
        <></>
      )}
      <div style={{ margin: "0 60px", paddingTop: "10px" }}>
        <Span
          icon={<MdCollectionsBookmark color="fff" fontSize={24} />}
          text="Collections"
          rectLeftWidth={150}
          rectRightWidth={
            info.category.length < 7
              ? info.category.length * 20 + 45
              : info.category.length * 17 + 45 > 190
              ? 170
              : info.category.length * 17 + 45
          }
          rectText={info.category.toUpperCase()}
        />
        <div id="bookDetail">
          <div id="frame">
            <img src={info.image} alt={info.name} />
          </div>
          <div id="detail">
            <p id="bookName">{formatStr(info.name.toUpperCase(), 63)}</p>
            <div id="caption">
              <span style={{ marginRight: "5px" }}>
                {info.rating !== null ? info.rating : 5}
              </span>
              <FaStar color="ffc107" />
              <svg height="30" width="30">
                <line
                  x1="15"
                  y1="5"
                  x2="15"
                  y2="25"
                  style={{ stroke: "#999999", strokeWidth: 2 }}
                />
              </svg>
              <span>{info.quantitySold} Sold</span>
              <svg height="30" width="30">
                <line
                  x1="15"
                  y1="5"
                  x2="15"
                  y2="25"
                  style={{ stroke: "#999999", strokeWidth: 2 }}
                />
              </svg>
              <p style={{ float: "right" }}>
                {info.quantityInStock} books available
              </p>
            </div>
            <p id="price">{`${info.sellingPrice}$`}</p>
            <p id="vat">*Product prices excluding VAT</p>
            <p id="description">Description:</p>
            <p>Author: {info.author}</p>
            {/* <p>{info.description}</p> */}
            {/* <p style={{textAlign: "justify"}}>What I Learned from the Trees delves into the intricate relationship between humans and nature, and how these often overlooked, everyday interactions affect us as individuals, families, and communities. </p> */}
            <p style={{ textAlign: "justify" }}>
              {formatStr(info.description, 330)}
            </p>
            <div id="quantity">
              <span id="description">Quantity:</span>
              <div style={{ display: "inline-block", marginLeft: "20px" }}>
                <span className="selectAmount" onClick={() => addAmount(false)}>
                  <IoRemove />
                </span>
                <input
                  type="number"
                  value={amount}
                  onChange={handleChangeAmount}
                />
                <span className="selectAmount" onClick={() => addAmount(true)}>
                  <IoAdd />
                </span>
              </div>
              <br />
              {/* <p style={{float: "right"}}>{info.quantityInStock} books available</p> */}
            </div>
            <Button
              variant="contained"
              color="primary"
              style={{ position: "absolute", bottom: "20px", right: "40px" }}
              onClick={handleAddToCart}
            >
              Add to cart
            </Button>
          </div>
        </div>
        <BookDetail {...info} />
        <Span
          icon={<AiFillThunderbolt color="fff" fontSize={24} />}
          text="Recommend"
          rectLeftWidth={170}
        />
        {recommend.length > 0 && (
          <div id="books">
            <BookCarousel books={recommend} />
          </div>
        )}
        {info.id !== 0 && <Comment id={info.id} rate={info.rating} />}
      </div>
      <br />
    </div>
  );
};

export default Product;
