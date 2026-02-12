import type { Transition } from "framer-motion";

export const cinematicSpring: Transition = {
  type: "spring",
  stiffness: 200,
  damping: 20,
  mass: 0.8,
};

export interface ShelfBookSpec {
  id: string;
  title: string;
  toneClass: string;
  edgeClass: string;
  heightClass: string;
  rotateY: number;
  zDepth: number;
}

export const EMAIL_BOOK_INDEX = 2;
export const PASSWORD_BOOK_INDEX = 5;
export const LOGIN_BOOK_INDEX = 7;
export const SHIFTING_BOOK_INDEX = 1;

export const SHELF_BOOKS: ShelfBookSpec[] = [
  {
    id: "book-history",
    title: "World History",
    toneClass: "from-[#7f5b3e] to-[#62442f]",
    edgeClass: "from-[#b9956f] to-[#9d7a54]",
    heightClass: "h-36",
    rotateY: 6,
    zDepth: 28,
  },
  {
    id: "book-poetry",
    title: "Poetry",
    toneClass: "from-[#3f2f2a] to-[#2d211d]",
    edgeClass: "from-[#9f7b62] to-[#7d5e4a]",
    heightClass: "h-32",
    rotateY: 4,
    zDepth: 36,
  },
  {
    id: "book-physics",
    title: "Classical Physics",
    toneClass: "from-[#5f4f4a] to-[#463833]",
    edgeClass: "from-[#b09078] to-[#8f725e]",
    heightClass: "h-40",
    rotateY: 5,
    zDepth: 42,
  },
  {
    id: "book-literature",
    title: "Literature",
    toneClass: "from-[#1f3246] to-[#162638]",
    edgeClass: "from-[#7f95ac] to-[#60758b]",
    heightClass: "h-34",
    rotateY: 6,
    zDepth: 33,
  },
  {
    id: "book-economics",
    title: "Economics",
    toneClass: "from-[#4a3a4f] to-[#342739]",
    edgeClass: "from-[#9f8aa8] to-[#7d6888]",
    heightClass: "h-38",
    rotateY: 5,
    zDepth: 38,
  },
  {
    id: "book-math",
    title: "Applied Math",
    toneClass: "from-[#3f4d5a] to-[#2b3640]",
    edgeClass: "from-[#8ca0b3] to-[#6f8293]",
    heightClass: "h-35",
    rotateY: 4,
    zDepth: 44,
  },
  {
    id: "book-design",
    title: "Design Systems",
    toneClass: "from-[#654739] to-[#4a3329]",
    edgeClass: "from-[#be9c81] to-[#9f7f68]",
    heightClass: "h-37",
    rotateY: 6,
    zDepth: 40,
  },
  {
    id: "book-ledger",
    title: "Scholar Ledger",
    toneClass: "from-[#0f2441] to-[#0c1a2f]",
    edgeClass: "from-[#8a9fb7] to-[#687e96]",
    heightClass: "h-42",
    rotateY: 5,
    zDepth: 48,
  },
  {
    id: "book-notes",
    title: "Notes",
    toneClass: "from-[#5c4031] to-[#3f2b21]",
    edgeClass: "from-[#c3a282] to-[#9d7f66]",
    heightClass: "h-33",
    rotateY: 4,
    zDepth: 34,
  },
];

