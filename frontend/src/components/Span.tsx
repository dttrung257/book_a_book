import { SpanProps } from "../models";

const Span = (span: SpanProps) => {
  return (
    <div
      style={{ display: "flex", alignItems: "center", position: "relative" }}
    >
      <svg
        width={
          span.rectLeftWidth +
          (span.rectRightWidth !== undefined ? span.rectRightWidth : 45)
        }
        height="60"
      >
        {span.rectRightWidth !== undefined ? (
          <svg>
            <rect
              x={span.rectLeftWidth - 10}
              y="10"
              rx="5"
              ry="5"
              width={span.rectRightWidth}
              height="35"
              style={{ fill: "#fff", stroke: "#E0DFE0", strokeWidth: 0.5 }}
            />
            <text
              x={span.rectRightWidth + span.rectLeftWidth - 30}
              y="35"
              fill="#008b8b"
              fontSize={17}
              fontFamily="system-ui"
              fontWeight={700}
              textAnchor="end"
            >
              {span.rectText}
            </text>
          </svg>
        ) : (
          <></>
        )}
        <polygon
          points={`${span.rectLeftWidth - 3},10 ${span.rectLeftWidth - 3},45 ${
            40 + span.rectLeftWidth
          },27.5`}
          style={{ fill: "#008B8B" }}
        />
        <rect
          x="0"
          y="10"
          rx="5"
          ry="5"
          width={span.rectLeftWidth}
          height="35"
          style={{ fill: "#008B8B" }}
        />
        <text
          x={40}
          y="35"
          fill="#fff"
          fontSize={22}
          fontFamily="system-ui"
          fontWeight={600}
        >
          {span.text}
        </text>
      </svg>
      <div
        style={{
          display: "inline-block",
          height: "35px",
          position: "absolute",
          left: "7px",
        }}
      >
        {span.icon}
      </div>
    </div>
  );
};

export default Span;
