import { useState } from "react";
import { Carousel, CarouselItem } from "react-bootstrap";
import { BannerData } from "./BannerData";
const CategoryBanner = (props: { category: string }) => {
  const [index, setIndex] = useState(0);
  type ObjectKey = keyof typeof BannerData;
  const banner = props.category as ObjectKey;
  const handleChangeSlide = (selected: number) => {
    setIndex(selected);
  };
  return (
    <Carousel
      activeIndex={index}
      onSelect={handleChangeSlide}
      className="w-100"
      indicators={false}
      controls={false}
    >
      {BannerData[banner].map((img) => (
        <CarouselItem className="imgBanner">
          <img
            src={`${img}`}
            alt="error"
            className="d-block w-100"
            height={150}
          />
        </CarouselItem>
      ))}
    </Carousel>
  );
};
export default CategoryBanner;
