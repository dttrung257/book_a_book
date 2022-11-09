import { SpanProps } from "../models";

const Span = (span: SpanProps) => {
  return (
    <div style={{ display: "flex", alignItems: "center" }}>
      <div style={{ display: "inline" }}>
        <svg
          width={
            span.rectLeftWidth +
            (span.rectRightWidth !== undefined ? span.rectRightWidth : 0)
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
                x={span.rectRightWidth + span.rectLeftWidth - 60}
                y="35"
                fill="#008b8b"
                fontSize={24}
                fontFamily="system-ui"
                fontWeight={500}
              >
                {span.rectText}
              </text>
            </svg>
          ) : (
            <></>
          )}
          <polygon
            points={`${span.rectLeftWidth - 3},10 ${
              span.rectLeftWidth - 3
            },45 ${40 + span.rectLeftWidth},27.5`}
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
            x={30}
            y="35"
            fill="#fff"
            fontSize={24}
            fontFamily="system-ui"
            fontWeight={600}
          >
            {span.text}
          </text>
        </svg>
      </div>
      <div
        style={{
          display: "inline-block",
          height: "35px",
          marginLeft: `-${
            span.rectLeftWidth +
            (span.rectRightWidth !== undefined ? span.rectRightWidth : 0) -
            5
          }px`,
        }}
      >
        {span.icon}
      </div>
    </div>
  );
};

export default Span;
