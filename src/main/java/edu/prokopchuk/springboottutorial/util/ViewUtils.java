package edu.prokopchuk.springboottutorial.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;

public final class ViewUtils {

  private ViewUtils() {}

  public static void fillPageWithTable(ModelMap modelMap,
                                       Page<?> content,
                                       String tableName) {
    // we increase it by 1, because numeration in Spring Page begins with 0
    int currentPageNumber = content.getPageable().getPageNumber() + 1;

    modelMap.addAttribute("currentPageNumber", currentPageNumber);
    modelMap.addAttribute(tableName, content);

    int totalPages = content.getTotalPages();

    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
          .boxed()
          .collect(Collectors.toList());
      modelMap.addAttribute("pageNumbers", pageNumbers);
    }
  }

}
