import OwlCarousel from "react-owl-carousel";
import Book from "../Book/Book";
import { BookInfoBrief } from "../../models";
import "./index.css";

const BookCarousel = (props: { books: BookInfoBrief[] }) => {
  const list = props.books;
  console.log(list);
  const prev =
    "<div class='nav-btn prev-slide'><svg class='icon'><path d='M17.51 3.87L15.73 2.1 5.84 12l9.9 9.9 1.77-1.77L9.38 12l8.13-8.13z' /></svg></div>";
  const next =
    "<div class='nav-btn next-slide'><svg class='icon'><path d='M6.23 20.23L8 22l10-10L8 2 6.23 3.77 14.46 12z' /></svg></div>";
  const options = {
    margin: 10,
    responsiveClass: true,
    nav: true,
    dots: false,
    autoplay: false,
    navText: [prev, next],
    smartSpeed: 1000,
    responsive: {
      0: {
        items: 2,
      },
      500: {
        items: 3,
      },
      768: {
        items: 4,
      },
      1000: {
        items: 5,
      },
    },
  };

  return (
    <OwlCarousel className="owl-theme" {...options}>
      {list.map((book, i) => {
        return (
          <div key={i}>
            <Book bestSeller={book} />
          </div>
        );
      })}
    </OwlCarousel>
  );
};

export default BookCarousel;
