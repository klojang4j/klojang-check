package nl.naturalis.common;

import nl.naturalis.check.Check;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;

import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.common.x.Param.ARRAY;

/**
 * Math-related methods. Quite a few methods in this class are about laying out a
 * one-dimensional array of values across one or more stack with a certain number of
 * rows and columns. However, for "grid" one might just as well read "matrix", or
 * "table", or "grid", or "HTML table", or "HTML grid" - whatever makes sense given
 * the context in which you use the methods.
 *
 * @author Ayco Holleman
 */
public final class MathMethods {

  private static final String ITEM_INDEX = "item index";
  private static final String ITEM_COUNT = "item count";
  private static final String ROW_COUNT = "row count";
  private static final String ROWS = "rows";
  private static final String COLS = "cols";

  //@formatter:off
  private static final Map<Class<?>, UnaryOperator<? extends Number>> absFunctions = Map.of(
      Integer.class,        n -> n.intValue() >= 0 ? n : Integer.valueOf(-n.intValue()),
      AtomicInteger.class,  n -> n.intValue() >= 0 ? n : new AtomicInteger(-n.intValue()),
      Double.class,         n -> n.doubleValue() >= 0 ? n : Double.valueOf(-n.doubleValue()),
      Long.class,           n -> n.longValue() >= 0 ? n : Long.valueOf(-n.longValue()),
      AtomicLong.class,     n -> n.longValue() >= 0 ? n : new AtomicLong(-n.longValue()),
      Float.class,          n -> n.floatValue() >= 0 ? n : Float.valueOf(-n.floatValue()),
      Short.class,          n -> n.shortValue() >= 0 ? n : Short.valueOf((short) -n.shortValue()),
      Byte.class,           n -> n.byteValue() >= 0 ? n : Byte.valueOf((byte) -n.byteValue()),
      BigInteger.class,     n -> ((BigInteger) n).abs(),
      BigDecimal.class,     n -> ((BigDecimal) n).abs());
  //@formatter:on

  private MathMethods() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the absolute value of an arbitrary type of {@linkplain Number}. Besides
   * the primitive number wrappers, this method supports {@link BigInteger},
   * {@link BigDecimal}, {@link AtomicInteger} and {@link AtomicLong}. An
   * {@link IllegalArgumentException} is thrown if the argument is any other type of
   * {@code Number}.
   *
   * @param <T> the type of the {@code Number}
   * @param number the number
   * @return the argument if it is a non-negative {@code Number}, else a
   *     {@code Number} representing the absolute value of the argument.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T extends Number> T abs(T number) {
    Check.notNull(number);
    UnaryOperator op = absFunctions.get(number.getClass());
    Check.that(op).is(notNull(), "Unsupported number type: ${type}");
    return (T) op.apply(number);
  }

  /**
   * Converts to arguments to {@code double}, then divides the first argument by the
   * second, and then applies {@link Math#ceil(double) Math.ceil}. Since this is a
   * very low-level operation, no argument-checking is performed.
   *
   * @param value The integer to divide
   * @param divideBy The integer to divide it by
   * @return the result of the division, rounded to the next integer
   */
  public static int divUp(int value, int divideBy) {
    return (int) Math.ceil((double) value / (double) divideBy);
  }

  /**
   * Equivalent to {@code value / dividedBy}. Usable a method reference. Since this
   * is a very low-level operation, no argument-checking is performed.
   *
   * @param value The integer to divide
   * @param divideBy The integer to divide it by
   * @return the result of the division, rounded to the preceding integer
   */
  public static int divDown(int value, int divideBy) {
    return value / divideBy;
  }

  /**
   * Divides the specified value by the specified denominator, rounding up if the
   * remainder is exactly {@code 0.5} (given double-precision calculation). Since
   * this is a very low-level operation, no argument-checking is performed.
   *
   * @param value The integer to divide
   * @param divideBy The integer to divide it by
   * @return the result of the division, rounded up if the remainder is exactly
   *     {@code 0.5}
   */
  public static int divHalfUp(int value, int divideBy) {
    return (int) Math.floor(0.5D + (double) value / (double) divideBy);
  }

  /**
   * Divides the specified value by the specified denominator, rounding down if the
   * remainder is exactly {@code 0.5} (given double-precision calculation). Since
   * this is a very low-level operation, no argument-checking is performed.
   *
   * @param value The integer to divide
   * @param divideBy The integer to divide it by
   * @return the result of the division, rounded down if the remainder is exactly
   *     {@code 0.5}
   */
  public static int divHalfDown(int value, int divideBy) {
    return (int) Math.ceil(-0.5D + (double) value / (double) divideBy);
  }

  /**
   * Returns the number of {@code rows x cols} stack needed to contain the specified
   * number of items.
   *
   * @param itemCount The total number of items
   * @param rows the number of rows per grid (or table, or matrix, or grid)
   * @param cols the number of columns per grid (or table, or matrix, or grid)
   * @return the number of stack needed to contain the specified number of items
   */
  public static int getGridCount(int itemCount, int rows, int cols) {
    Check.that(itemCount, ITEM_COUNT).isNot(negative())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    return divUp(itemCount, rows * cols);
  }

  /**
   * Returns the number of grids needed to contain the specified number of rows.
   *
   * @param rowCount the total number of rows
   * @param rows The number of rows per grid
   * @return the number of stack needed to contain the specified number of rows
   */
  public static int getGridCount(int rowCount, int rows) {
    Check.that(rowCount, ROW_COUNT).isNot(negative()).and(rows, ROWS).is(positive());
    return divUp(rowCount, rows);
  }

  /**
   * Returns the array index of the last grid within an array of stack containing the
   * specified number rows. So basically {@link #getGridCount(int, int) getGridCount}
   * minus one.
   *
   * @param rowCount the total number of rows
   * @param rows The number of rows per grid
   * @return the index of the last grid within an array of stack
   */
  public static int indexOfLastGrid(int rowCount, int rows) {
    Check.that(rowCount, ROW_COUNT).isNot(negative()).and(rows, ROWS).is(positive());
    return rowCount == 0 ? 0 : (rowCount - 1) / rows;
  }

  /**
   * Returns the number of <i>populated</i> rows in the last grid (with the very last
   * row possibly only partly populated).
   *
   * @param rowCount the total number of rows
   * @param rows the number of rows per grid
   * @return the number of (partly) populated rows on the last grid
   */
  public static int countRowsOnLastGrid(int rowCount, int rows) {
    Check.that(rowCount, ROW_COUNT).isNot(negative()).and(rows, ROWS).is(positive());
    if (rowCount != 0) {
      int x;
      return (x = rowCount % rows) == 0 ? rows : x;
    }
    return 0;
  }

  /**
   * Returns the number of trailing, empty rows in the last grid.
   *
   * @param rowCount the total number of rows
   * @param rows the number of rows per grid
   * @return the number of trailing, empty rows in the last grid
   */
  public static int countEmptyRowsOnLastGrid(int rowCount, int rows) {
    Check.that(rowCount, ROW_COUNT).isNot(negative()).and(rows, ROWS).is(positive());
    if (rowCount != 0) {
      int x;
      return (x = rowCount % rows) == 0 ? 0 : rows - x;
    }
    return 0;
  }

  /**
   * Returns the array index of the grid within an array of stack that will contain
   * the item with the specified index in a one-dimensional array. In other words,
   * this method maps an index in a one-dimensional array ({@code T[x]}) to an index
   * in the first component of a three-dimensional array ({@code T[y][][]}.
   *
   * @param itemIndex the array index of the item
   * @param rows the number of rows per grid (or table, or matrix, or grid)
   * @param cols the number of columns per grid (or table, or matrix, or grid)
   * @return the index of the
   */
  public static int getGrid(int itemIndex, int rows, int cols) {
    Check.that(itemIndex, ITEM_INDEX).isNot(negative())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    return itemIndex / (rows * cols);
  }

  /**
   * Returns the row index of an item, relative to the grid that it finds itself on.
   * In other words, this method maps an index in a one-dimensional array
   * ({@code T[x]}) to an index in the second component of a three-dimensional array
   * ({@code T[][y][]}.
   *
   * @param itemIndex the array index of the item
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @return the row index of an item
   */
  public static int getRow(int itemIndex, int rows, int cols) {
    Check.that(itemIndex, ITEM_INDEX).isNot(negative())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    return (itemIndex / cols) % rows;
  }

  /**
   * Returns the column index of an item with the specified array index, given the
   * specified number of columns per grid. In other words, this method maps an index
   * in a one-dimensional array ({@code T[x]}) to an index in the third component of
   * a three-dimensional array ({@code T[][][y]}.
   *
   * @param itemIndex the array index of the item
   * @param cols the number of columns per grid
   * @return the column index of an item with the specified array index
   */
  public static int getColumn(int itemIndex, int cols) {
    Check.that(itemIndex, ITEM_INDEX).isNot(negative())
        .and(cols, COLS).is(positive());
    return itemIndex % cols;
  }

  /**
   * Returns the row index of an item in a column-major layout. That is: the items
   * are laid out in columns (top-to-bottom first, left-to-right second).
   *
   * @param itemIndex the array index of the item
   * @param rows the number of rows per grid
   * @return the row index of an item in a column-major layout
   */
  public static int getRowCM(int itemIndex, int rows) {
    Check.that(itemIndex, ITEM_INDEX).isNot(negative())
        .and(rows, ROWS).is(positive());
    return itemIndex % rows;
  }

  /**
   * Returns the column index of an item in a column-major layout.
   *
   * @param itemIndex the array index of the item
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @return the column number of an item in a column-major layout
   */
  public static int getColumnCM(int itemIndex, int rows, int cols) {
    Check.that(itemIndex, ITEM_INDEX).isNot(negative())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    return (itemIndex / rows) % cols;
  }

  /**
   * Returns grid, row, and column index of an item with the specified array index.
   *
   * @param itemIndex the array index of the item
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @return the grid, row, and column index of an item with the specified array
   *     index
   */
  public static int[] getGridRowColumn(int itemIndex, int rows, int cols) {
    Check.that(itemIndex, ITEM_INDEX).isNot(negative())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    return new int[] {itemIndex / (rows * cols),
        (itemIndex / cols) % rows,
        itemIndex % cols};
  }

  /**
   * Returns the grid, row, and column index of an item in a column-major layout.
   *
   * @param itemIndex the array index of the item
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @return the grid, row, and column number of an item in a column-major layout
   */
  public static int[] getGridRowColumnCM(int itemIndex, int rows, int cols) {
    Check.that(itemIndex, ITEM_INDEX).isNot(negative())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    return new int[] {itemIndex / (rows * cols),
        itemIndex % rows,
        (itemIndex / rows) % cols};
  }

  /**
   * Converts a one-dimensional array of values into zero or more grids of
   * {@code rows x cols} cells. Empty cells in the last grid will be padded with
   * zeros.
   *
   * @param values The values to rasterize
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @return Zero or more grids containing the values in the array, laid out in rows
   *     containing {@code cols} cells
   */
  public static int[][][] toGrid(int[] values, int rows, int cols) {
    Check.that(values, ARRAY).is(notNull())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    int cellsPerGrid = rows * cols;
    int grids = getGridCount(values.length, rows, cols);
    int[][][] stack = new int[grids][rows][cols];
    MAIN_LOOP:
    for (int grid = 0; grid < grids; ++grid) {
      int gridOffset = grid * cellsPerGrid;
      for (int row = 0; row < rows; ++row) {
        int rowOffset = gridOffset + row * cols;
        for (int col = 0; col < cols; ++col) {
          int idx = rowOffset + col;
          if (idx >= values.length) {
            break MAIN_LOOP;
          }
          stack[grid][row][col] = values[idx];
        }
      }
    }
    return stack;
  }

  /**
   * Converts a one-dimensional array of values into zero or more grids of
   * {@code rows x cols} cells.
   *
   * @param values The values to rasterize
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @param padValue The value to pad the empty cells in the last grid with
   * @return Zero or more grids containing the values in the array, laid out in rows
   *     containing {@code cols} cells
   */
  public static int[][][] toGrid(int[] values, int rows, int cols, int padValue) {
    Check.that(values, ARRAY).is(notNull())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    int cellsPerGrid = rows * cols;
    int grids = getGridCount(values.length, rows, cols);
    int[][][] stack = new int[grids][rows][cols];
    for (int grid = 0; grid < grids; ++grid) {
      int gridOffset = grid * cellsPerGrid;
      for (int row = 0; row < rows; ++row) {
        int rowOffset = gridOffset + row * cols;
        for (int col = 0; col < cols; ++col) {
          int idx = rowOffset + col;
          if (idx < values.length) {
            stack[grid][row][col] = values[idx];
          } else {
            stack[grid][row][col] = padValue;
          }
        }
      }
    }
    return stack;
  }

  /**
   * Converts a one-dimensional array of values into zero or more grids of
   * {@code rows x cols} cells. Empty cells in the last grid will be padded with
   * {@code null} values.
   *
   * @param values The values to rasterize
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @param <T> The type of the values to be rasterized
   * @return Zero or more grids containing the values in the array, laid out in rows
   *     containing {@code cols} cells
   */
  public static <T> T[][][] toGrid(T[] values, int rows, int cols) {
    Check.that(values, ARRAY).is(notNull())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    int cellsPerGrid = rows * cols;
    int grids = getGridCount(values.length, rows, cols);
    T[][][] stack = createEmptyGrids(values, grids, rows, cols);
    MAIN_LOOP:
    for (int grid = 0; grid < grids; ++grid) {
      int gridOffset = grid * cellsPerGrid;
      for (int row = 0; row < rows; ++row) {
        int rowOffset = gridOffset + row * cols;
        for (int col = 0; col < cols; ++col) {
          int idx = rowOffset + col;
          if (idx >= values.length) {
            break MAIN_LOOP;
          }
          stack[grid][row][col] = values[idx];
        }
      }
    }
    return stack;
  }

  /**
   * Converts a one-dimensional array of values into zero or more grids of
   * {@code rows x cols} cells.
   *
   * @param values The values to rasterize
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @param padValue The value to pad the empty cells in the last grid with
   * @param <T> The type of the values to be rasterized
   * @return Zero or more grids containing the values in the array, laid out in rows
   *     containing {@code cols} cells
   */
  public static <T> T[][][] toGrid(T[] values, int rows, int cols, T padValue) {
    Check.that(values, ARRAY).is(notNull())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    int cellsPerGrid = rows * cols;
    int grids = getGridCount(values.length, rows, cols);
    T[][][] stack = createEmptyGrids(values, grids, rows, cols);
    for (int grid = 0; grid < grids; ++grid) {
      int gridOffset = grid * cellsPerGrid;
      for (int row = 0; row < rows; ++row) {
        int rowOffset = gridOffset + row * cols;
        for (int col = 0; col < cols; ++col) {
          int idx = rowOffset + col;
          if (idx < values.length) {
            stack[grid][row][col] = values[idx];
          } else {
            stack[grid][row][col] = padValue;
          }
        }
      }
    }
    return stack;
  }

  /**
   * Converts a one-dimensional array of values into zero or more grids of
   * {@code rows x cols} cells. Empty cells in the last grid will be padded with
   * zeros. The values are laid out in column-major fashion.
   *
   * @param values The values to rasterize
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @return Zero or more grids containing the values in the array, laid out in
   *     columns containing {@code cols} cells
   */
  public static int[][][] toGridCM(int[] values, int rows, int cols) {
    Check.that(values, ARRAY).is(notNull())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    int cellsPerGrid = rows * cols;
    int grids = getGridCount(values.length, rows, cols);
    int[][][] stack = new int[grids][rows][cols];
    MAIN_LOOP:
    for (int grid = 0; grid < grids; ++grid) {
      int gridOffset = grid * cellsPerGrid;
      for (int col = 0; col < cols; ++col) {
        int columnOffset = gridOffset + col * rows;
        for (int row = 0; row < rows; ++row) {
          int idx = columnOffset + row;
          if (idx >= values.length) {
            break MAIN_LOOP;
          }
          stack[grid][row][col] = values[idx];
        }
      }
    }
    return stack;
  }

  /**
   * Converts a one-dimensional array of values into zero or more grids of
   * {@code rows x cols} cells. The values are laid out in column-major fashion.
   *
   * @param values The values to rasterize
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @param padValue The value to pad the empty cells in the last grid with
   * @return Zero or more grids containing the values in the array, laid out in
   *     columns containing {@code cols} cells
   */
  public static int[][][] toGridCM(int[] values, int rows, int cols, int padValue) {
    Check.that(values, ARRAY).is(notNull())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    int cellsPerGrid = rows * cols;
    int grids = getGridCount(values.length, rows, cols);
    int[][][] stack = new int[grids][rows][cols];
    for (int grid = 0; grid < grids; ++grid) {
      int gridOffset = grid * cellsPerGrid;
      for (int col = 0; col < cols; ++col) {
        int columnOffset = gridOffset + col * rows;
        for (int row = 0; row < rows; ++row) {
          int idx = columnOffset + row;
          if (idx < values.length) {
            stack[grid][row][col] = values[idx];
          } else {
            stack[grid][row][col] = padValue;
          }
        }
      }
    }
    return stack;
  }

  /**
   * Converts a one-dimensional array of values into zero or more grids of
   * {@code rows x cols} cells. Empty cells in the last grid will be padded with
   * {@code null} values. The values are laid out in column-major fashion.
   *
   * @param values The values to rasterize
   * @param rows the number of rows per raster (or table, or matrix)
   * @param cols the number of columns per raster (or table, or matrix)
   * @param <T> The type of the values to be rasterized
   * @return Zero or more grids containing the values in the array, laid out in
   *     columns containing {@code cols} cells
   */
  public static <T> T[][][] toGridCM(T[] values, int rows, int cols) {
    Check.that(values, ARRAY).is(notNull())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    int cellsPerGrid = rows * cols;
    int grids = getGridCount(values.length, rows, cols);
    T[][][] stack = createEmptyGrids(values, grids, rows, cols);
    MAIN_LOOP:
    for (int grid = 0; grid < grids; ++grid) {
      int gridOffset = grid * cellsPerGrid;
      for (int col = 0; col < cols; ++col) {
        int columnOffset = gridOffset + col * rows;
        for (int row = 0; row < rows; ++row) {
          int idx = columnOffset + row;
          if (idx >= values.length) {
            break MAIN_LOOP;
          }
          stack[grid][row][col] = values[idx];
        }
      }
    }
    return stack;
  }

  /**
   * Converts a one-dimensional array of values into zero or more grids of
   * {@code rows x cols} cells. The values are laid out in column-major fashion.
   *
   * @param values The values to rasterize
   * @param rows the number of rows per grid
   * @param cols the number of columns per grid
   * @param padValue The value to pad the empty cells in the last grid with
   * @param <T> The type of the values to be rasterized
   * @return Zero or more grids containing the values in the array, laid out in
   *     columns containing {@code cols} cells
   */
  public static <T> T[][][] toGridCM(T[] values, int rows, int cols, T padValue) {
    Check.that(values, ARRAY).is(notNull())
        .and(rows, ROWS).is(positive())
        .and(cols, COLS).is(positive());
    int cellsPerGrid = rows * cols;
    int grids = getGridCount(values.length, rows, cols);
    T[][][] stack = createEmptyGrids(values, grids, rows, cols);
    for (int grid = 0; grid < grids; ++grid) {
      int gridOffset = grid * cellsPerGrid;
      for (int col = 0; col < cols; ++col) {
        int columnOffset = gridOffset + col * rows;
        for (int row = 0; row < rows; ++row) {
          int idx = columnOffset + row;
          if (idx < values.length) {
            stack[grid][row][col] = values[idx];
          } else {
            stack[grid][row][col] = padValue;
          }
        }
      }
    }
    return stack;
  }

  @SuppressWarnings({"unchecked"})
  private static <T> T[][][] createEmptyGrids(T[] values,
      int grids,
      int rows,
      int cols) {
    return (T[][][]) Array.newInstance(values.getClass()
        .getComponentType(), grids, rows, cols);
  }

}
