import type { Transition } from "framer-motion";

export interface MotionTokenSet {
  snappy: Transition;
  smooth: Transition;
  float: Transition;
}

export const springTokens: MotionTokenSet = {
  snappy: {
    type: "spring",
    stiffness: 420,
    damping: 30,
    mass: 0.8,
  },
  smooth: {
    type: "spring",
    stiffness: 260,
    damping: 28,
    mass: 1,
  },
  float: {
    type: "spring",
    stiffness: 140,
    damping: 20,
    mass: 1.2,
  },
};

