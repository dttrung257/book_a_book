import { Book } from "../../models";
import "./index.css";

const BookDetail = (book: Book) => {
  return (
    <div id="bookDetailCont">
      <div id="bookSpecification">
        <p className="title">Book Specifications</p>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">Name</div>
          <div>
            <p>{book.name}</p>
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">Author</div>
          <div>
            <p>{book.author !== null ? book.author : "Unknown"}</p>
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">Category</div>
          <div>
            <p>{book.category !== null ? book.category : "Unknown"}</p>
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">Publisher</div>
          <div>
            <p>{book.publisher !== null ? book.publisher : "Unknown"}</p>
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">Publication year</div>
          <div>
            <p>
              {book.yearOfPublication !== null
                ? book.yearOfPublication
                : "Unknown"}
            </p>
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">ISBN</div>
          <div>
            <p>{book.isbn !== null ? book.isbn : "Unknown"}</p>
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">Pages</div>
          <div>
            <p>
              {book.numberOfPages !== null ? book.numberOfPages : "Unknown"}
            </p>
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">Dimensions</div>
          <div>
            <p>
              {book.width !== null ? book.width : "Unknown"} x{" "}
              {book.height !== null ? book.height : "Unknown"}
            </p>
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div className="specificationTitle">Stock</div>
          <div>
            <p>
              {book.quantityInStock !== null ? book.quantityInStock : "Unknown"}
            </p>
          </div>
        </div>
      </div>
      <div id="privacy">
        <p className="title">Packaging & Delivery</p>
        <p style={{ fontSize: "17px" }}>
          All our books are securely packaged using the most appropriate
          materials to ensure that your book arrives safely.
          <br />
          We recommend that you opt for a fully-insured delivery method, the
          price for which varies depending on the value of the book(s).
        </p>
        <p className="title">Returns</p>
        <p style={{ fontSize: "17px" }}>
          Incorrectly described, significantly damaged on arrival or improperly
          packaged.
          <br />
          If you no longer want the item, or you purchased it by mistake, you
          are welcome to return the item to us for a refund, but this is at cost
          to you.
        </p>
      </div>
      {/* <div
        style={{
          width: "45%",
          display: "block",
          margin: "auto",
          backgroundColor: "#fff",
          padding: "30px 60px",
        }}
      >
        <p
          style={{
            fontSize: "24px",
            fontWeight: "600",
            marginBottom: "20px",
            color: "#000",
          }}
        >
          Book Specifications
        </p>
        <div style={{ display: "flex" }}>
          <div
            style={{
              fontSize: "18px",
              fontWeight: "600",
              color: "#666",
              width: "35%",
            }}
          >
            <p>Name</p>
            <p>Author</p>
            <p>Category</p>
            <p>Publisher</p>
            <p>Published year</p>
            <p>ISBN</p>
            <p>Pages</p>
            <p>Size</p>
            <p>Stock</p>
          </div>
          <div style={{ fontSize: "18px" }}>
            <p>{book.name}</p>
            <p>{book.author}</p>
            <p>{book.category}</p>
            <p>{book.publisher !== null ? book.publisher : "Unknown"}</p>
            <p>
              {book.yearOfPublication !== null
                ? book.yearOfPublication
                : "Unknown"}
            </p>
            <p>{book.isbn !== null ? book.isbn : "Unknown"}</p>
            <p>
              {book.numberOfPages !== null ? book.numberOfPages : "Unknown"}
            </p>
            <p>
              {book.width !== null ? book.width : "Unknown"} x{" "}
              {book.height !== null ? book.height : "Unknown"}
            </p>
            <p>{book.quantityInStock}</p>
          </div>
        </div>
      </div>
      <div
        style={{
          width: "45%",
          display: "block",
          margin: "auto",
          backgroundColor: "#fff",
          padding: "30px 60px",
        }}
      >
        <p
          style={{
            height: "100%",
            fontSize: "24px",
            fontWeight: "600",
            marginBottom: "20px",
            color: "#000",
          }}
        >
          Packaging & Delivery
        </p>
        <p style={{ fontSize: "18px" }}>
          All our books are securely packaged using the most appropriate
          materials to ensure that your book arrives safely.
          <br />
          We recommend that you opt for a fully-insured delivery method, the
          price for which varies depending on the value of the book(s).
        </p>
        <p
          style={{
            height: "100%",
            fontSize: "24px",
            fontWeight: "600",
            marginBottom: "20px",
            color: "#000",
          }}
        >
          Returns
        </p>
        <p style={{ fontSize: "18px" }}>
          Incorrectly described, significantly damaged on arrival or improperly
          packaged.
          <br />
          If you no longer want the item, or you purchased it by mistake, you
          are welcome to return the item to us for a refund, but this is at cost
          to you.
        </p>
      </div> */}
    </div>
  );
};

export default BookDetail;
