import { useState } from "react";
import { Carousel } from "react-bootstrap";

const Slide = () => {
  const [index, setIndex] = useState(0);
  let slideWidth = window.screen.width - 130;

  const handleSelect = (selectedIndex: number) => {
    setIndex(selectedIndex);
  };

  return (
    <Carousel
      activeIndex={index}
      onSelect={handleSelect}
      style={{ width: slideWidth, margin: "auto" }}
    >
      <Carousel.Item>
        <img
          className="d-block w-100"
          src="https://live.staticflickr.com/65535/52480147762_ff6e846e1b_b.jpg"
          alt="First slide"
        />
      </Carousel.Item>
      <Carousel.Item>
        <img
          className="d-block w-100"
          src="https://live.staticflickr.com/65535/52480147777_d0124d7ae3_k.jpg"
          alt="Second slide"
        />
      </Carousel.Item>
      <Carousel.Item>
        <img
          className="d-block w-100"
          src="https://live.staticflickr.com/65535/52481195938_6b5773a1e7_b.jpg"
          alt="Third slide"
        />
      </Carousel.Item>
      <Carousel.Item>
        <img
          className="d-block w-100"
          src="https://live.staticflickr.com/65535/52480147762_ff6e846e1b_b.jpg"
          alt="Third slide"
        />
      </Carousel.Item>
      <Carousel.Item>
        <img
          className="d-block w-100"
          src="https://live.staticflickr.com/65535/52480922309_e410fa54fb_k.jpg"
          alt="Third slide"
        />
      </Carousel.Item>
    </Carousel>
  );
};

export default Slide;
