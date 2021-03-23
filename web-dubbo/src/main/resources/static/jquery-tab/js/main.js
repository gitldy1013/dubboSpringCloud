$(function() {
  Waves.displayEffect();

 $(document).on("click",".tabs li", function() {

    // 切换 tab
    $(".tabs-item").removeClass("active");
    $(this).addClass("active");

    // 切换 iframe
    $(".iframe").removeClass("active");
    $("#iframe_" + $(this).attr("id")).addClass("active");

 });

 $("#nav li a").on("click", function() {
    TabManager.addTab(this);
 });

  var menu = new BootstrapMenu(".tabs li", {
    fetchElementData: function(item) {
       return item;
    },
    actions: {
      close: {
        name: "关闭",
        iconClass: "fa fa-close",
        onClick: function(item) {
          TabManager.closeTab(item);
        }
      },
      closeOther: {
        name: "关闭其他",
        iconClass: "fa fa-exchange",
        onClick: function(item) {
            var index = item.attr("id");
            $(".tabs li").each(function() {
                if($(this).attr("id") != index) {
                  TabManager.closeTab($(this));
                }
            });
        }
      },
      closeAll: {
        name: "关闭全部",
        iconClass: "fa fa-arrows",
        onClick: function() {
          $(".tabs li").each(function() {
                TabManager.closeTab($(this));
            });
        }
      },
      closeRight: {
        name: "关闭右侧所有",
        iconClass: "fa fa-arrow-right",
        onClick: function(item) {
          var index = item.attr("id");
          $($('.tabs li').toArray().reverse()).each(function() {
            if ($(this).attr("id") != index) {
              TabManager.closeTab($(this));
            } else {
              return false;
            }
          });
        }
      },
      closeLeft: {
        name: "关闭左侧所有",
        iconClass: "fa fa-arrow-left",
        onClick: function(item) {
          var index = item.attr("id");
          $('.tabs li').each(function() {
            if ($(this).attr("id") != index) {
              TabManager.closeTab($(this));
            } else {
              return false;
            }
          });
        }
      },
      refresh: {
        name: "刷新",
        iconClass: "fa fa-refresh",
        onClick: function(item) {
          var index = item.attr("id");
          var $iframe = $('#iframe_' + index).find('iframe');
          $iframe.attr('src', $iframe.attr('src'));
        }
      }
    }
  });

});

var TabManager = {
  addTab: function(obj) {
    // 取消激活
    $(".tabs-item").removeClass("active");
    $(".iframe").removeClass("active");

    var tabName = $(obj).text();
    var url = $(obj).data("url");
    var index = url.replace(/\./g, '_').replace(/\//g, '_').replace(/:/g, '_').replace(/\?/g, '_').replace(/,/g, '_').replace(/=/g, '_').replace(/&/g, '_');

    // 如果标签已存在，重新激活
    if ($("#tab_" + index).length > 0) {
      $("#tab_" + index).trigger("click");

    } else {
         // 创建 tab
      var tab = "<li class='tabs-item active' id='tab_"+index+"'><a href='javascript:;' class='waves-effect waves-light'>"+tabName+"</a></li>";
      $("#tabs").append(tab);

      // 创建 iframe
      var iframe = "<div id='iframe_tab_"+index+"' class='iframe active'><iframe src='"+url+"' frameborder='0' width='100%' height='780'></iframe></div>";
      $("#tab-container-content").append(iframe);
    }
  },
  closeTab: function(item) {

      // 如果移除的是激活的标签，则激活前一个标签
      if (item.hasClass("active")) {
        item.prev().trigger("click");
      }

      $("#iframe_" + item.attr("id")).remove();
      item.remove();
  }
}
