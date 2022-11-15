import "./index.css";

const Collections = () => {
  return (
    <div id="collection">
      <div className="coll">
        {/* literary */}
        <div className="span">
          <div>
            <p>LITERARY</p>
            <p className="spanAmount">100 books</p>
          </div>
        </div>
        <img src="https://live.staticflickr.com/65535/52447753485_f125a528bd_n.jpg" />
      </div>
      {/* lifestyle */}
      <div className="coll">
        <div className="span">
          <div>
            <p>LIFESTYLE</p>
            <p className="spanAmount">100 books</p>
          </div>
        </div>
        <img src="https://live.staticflickr.com/65535/52447360996_de6c10fa26_n.jpg" />
      </div>
      {/* technology */}
      <div className="coll">
        <div className="span">
          <div>
            <p>TECHNOLOGY</p>
            <p className="spanAmount">100 books</p>
          </div>
        </div>
        <img src="https://live.staticflickr.com/65535/52459960778_8f291abb41_n.jpg" />
      </div>
      {/* romance */}
      <div className="coll">
        <div className="span">
          <div>
            <p>ROMANCE</p>
            <p className="spanAmount">100 books</p>
          </div>
        </div>
        <img src="https://live.staticflickr.com/65535/52459529431_8755d42f00_w.jpg" />
      </div>
    </div>
  );
};

export default Collections;
