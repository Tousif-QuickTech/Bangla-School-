import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../controllers/bangla_controller.dart';
import '../../models/bangla_models.dart';

class ShadcnPersistentHeaderSearchBar extends StatefulWidget {
  const ShadcnPersistentHeaderSearchBar({Key? key}) : super(key: key);

  @override
  _ShadcnPersistentHeaderSearchBarState createState() => _ShadcnPersistentHeaderSearchBarState();
}

class _ShadcnPersistentHeaderSearchBarState extends State<ShadcnPersistentHeaderSearchBar> {
  final TextEditingController _queryController = TextEditingController();
  final FocusNode _focusNode = FocusNode();
  OverlayEntry? _overlayEntry;

  @override
  void initState() {
    super.initState();
    _focusNode.addListener(() {
      if (_focusNode.hasFocus) {
        _showSuggestionsOverlay();
      } else {
        _hideSuggestionsOverlay();
      }
    });
    _queryController.addListener(_onQueryChanged);
  }

  @override
  void dispose() {
    _focusNode.dispose();
    _queryController.dispose();
    _hideSuggestionsOverlay();
    super.dispose();
  }

  void _onQueryChanged() {
    if (_focusNode.hasFocus && _queryController.text.isNotEmpty) {
      _showSuggestionsOverlay();
    } else {
      _hideSuggestionsOverlay();
    }
  }

  void _showSuggestionsOverlay() {
    _hideSuggestionsOverlay();
    _overlayEntry = _createOverlayEntry();
    Overlay.of(context).insert(_overlayEntry!);
  }

  void _hideSuggestionsOverlay() {
    _overlayEntry?.remove();
    _overlayEntry = null;
  }

  OverlayEntry _createOverlayEntry() {
    RenderBox renderBox = context.findRenderObject() as RenderBox;
    var size = renderBox.size;
    var offset = renderBox.localToGlobal(Offset.zero);

    return OverlayEntry(
      builder: (context) {
        final controller = Provider.of<BanglaController>(context, listen: true);
        final query = _queryController.text.trim();

        if (query.isEmpty) return const SizedBox.shrink();

        final filteredBooks = controller.books.where((b) =>
            b.title.toLowerCase().contains(query.toLowerCase()) ||
            b.authorName.toLowerCase().contains(query.toLowerCase())).toList();

        final filteredCourses = controller.courses.where((c) =>
            c.title.toLowerCase().contains(query.toLowerCase()) ||
            c.instructor.toLowerCase().contains(query.toLowerCase())).toList();

        final filteredAuthors = controller.authors.where((a) =>
            a.name.toLowerCase().contains(query.toLowerCase()) ||
            a.bio.toLowerCase().contains(query.toLowerCase())).toList();

        final hasSuggestions = filteredBooks.isNotEmpty || filteredCourses.isNotEmpty || filteredAuthors.isNotEmpty;

        return Positioned(
          left: offset.dx + 16,
          top: offset.dy + size.height + 6,
          width: size.width - 32,
          child: Material(
            elevation: 12,
            borderRadius: BorderRadius.circular(16),
            color: Theme.of(context).cardColor,
            shadowColor: Colors.black45,
            child: Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(16),
                border: Border.all(
                  color: Theme.of(context).primaryColor.withOpacity(0.15),
                ),
              ),
              constraints: const BoxConstraints(maxHeight: 300),
              child: SingleChildScrollView(
                padding: const EdgeInsets.symmetric(vertical: 8),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    if (!hasSuggestions)
                      Padding(
                        padding: const EdgeInsets.symmetric(vertical: 24.0),
                        child: Center(
                          child: Column(
                            children: [
                              Icon(
                                Icons.find_in_page_outlined,
                                size: 36,
                                color: Theme.of(context).hintColor.withOpacity(0.5),
                              ),
                              const SizedBox(height: 8),
                              Text(
                                "কোনো ফলাফল পাওয়া যায়নি",
                                style: TextStyle(
                                  fontSize: 13,
                                  fontWeight: FontWeight.bold,
                                  color: Theme.of(context).hintColor.withOpacity(0.7),
                                ),
                              ),
                            ],
                          ),
                        ),
                      )
                    else ...[
                      if (filteredBooks.isNotEmpty) ...[
                        _buildGroupHeader("বইসমূহ", Icons.book_outlined),
                        ...filteredBooks.map((b) => _buildSuggestionRow(
                          b.title,
                          b.authorName,
                          "বই",
                          () {
                            _queryController.clear();
                            _focusNode.unfocus();
                            controller.navigateTo(ActiveScreen.bookDetails, itemId: b.id);
                          },
                        )),
                      ],
                      if (filteredCourses.isNotEmpty) ...[
                        _buildGroupHeader("কোর্সসমূহ", Icons.school_outlined),
                        ...filteredCourses.map((c) => _buildSuggestionRow(
                          c.title,
                          c.instructor,
                          "কোর্স",
                          () {
                            _queryController.clear();
                            _focusNode.unfocus();
                            controller.navigateTo(ActiveScreen.courseDetails, itemId: c.id);
                          },
                        )),
                      ],
                      if (filteredAuthors.isNotEmpty) ...[
                        _buildGroupHeader("লেখকবৃন্দ", Icons.people_outline),
                        ...filteredAuthors.map((a) => _buildSuggestionRow(
                          a.name,
                          a.bio,
                          "সাহিত্যিক",
                          () {
                            _queryController.clear();
                            _focusNode.unfocus();
                            controller.navigateTo(ActiveScreen.authorDetails, itemId: a.id);
                          },
                        )),
                      ],
                    ]
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  Widget _buildGroupHeader(String title, IconData icon) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Row(
        children: [
          Icon(icon, size: 14, color: Theme.of(context).primaryColor),
          const SizedBox(width: 6),
          Text(
            title,
            style: TextStyle(
              fontSize: 11,
              fontWeight: FontWeight.black,
              color: Theme.of(context).primaryColor,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSuggestionRow(String title, String subtitle, String badge, VoidCallback onClick) {
    return InkWell(
      onTap: onClick,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 10.0),
        child: Row(
          children: [
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    title,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    subtitle,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(fontSize: 11, color: Theme.of(context).hintColor.withOpacity(0.7)),
                  ),
                ],
              ),
            ),
            const SizedBox(width: 8),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
              decoration: BoxDecoration(
                color: Theme.of(context).primaryColor.withOpacity(0.08),
                borderRadius: BorderRadius.circular(6),
              ),
              child: Text(
                badge,
                style: TextStyle(
                  fontSize: 9,
                  fontWeight: FontWeight.bold,
                  color: Theme.of(context).primaryColor,
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 6.0),
      child: Container(
        height: 48,
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(12),
        ),
        child: TextField(
          controller: _queryController,
          focusNode: _focusNode,
          style: const TextStyle(fontSize: 13),
          decoration: InputDecoration(
            hintText: "বই, কোর্স বা লেখক খুঁজুন...",
            hintStyle: TextStyle(fontSize: 13, color: Theme.of(context).hintColor.withOpacity(0.6)),
            prefixIcon: Icon(Icons.search, size: 18, color: Theme.of(context).primaryColor),
            suffixIcon: _queryController.text.isNotEmpty
                ? IconButton(
                    icon: const Icon(Icons.clear, size: 16),
                    onPressed: () {
                      _queryController.clear();
                    },
                  )
                : null,
            contentPadding: const EdgeInsets.symmetric(vertical: 12),
            filled: true,
            fillColor: Theme.of(context).cardColor,
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide(color: Theme.of(context).dividerColor.withOpacity(0.1)),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide(color: Theme.of(context).primaryColor, width: 1.5),
            ),
          ),
        ),
      ),
    );
  }
}
