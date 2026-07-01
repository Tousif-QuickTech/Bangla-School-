package com.example.ui

import com.example.data.Course
import com.example.data.Book
import com.example.data.Author
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.LinearEasing
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

// --- Elegant Soft Literary Gradients ---
val LiteratureGradients = listOf(
    Brush.linearGradient(listOf(Color(0xFFFFD8A8), Color(0xFFFFA94D))), // Warm Orange
    Brush.linearGradient(listOf(Color(0xFFE5DBFF), Color(0xFFB197FC))), // Soft Lavender
    Brush.linearGradient(listOf(Color(0xFFD0EBFF), Color(0xFF74C0FC))), // Pastel Blue
    Brush.linearGradient(listOf(Color(0xFFC3FAE8), Color(0xFF63E6BE))), // Fresh Mint
    Brush.linearGradient(listOf(Color(0xFFFFD3E2), Color(0xFFFF87B2))), // Sunset Pink
    Brush.linearGradient(listOf(Color(0xFFFFE3E3), Color(0xFFFF8787)))  // Coral Red
)

@Composable
fun MockImage(
    name: String,
    title: String = "",
    subtitle: String = "",
    modifier: Modifier = Modifier,
    height: Int = 180
) {
    val drawableResId = when {
        name.contains("author", ignoreCase = true) -> com.example.R.drawable.img_author_mock_1782807400595
        name.contains("book", ignoreCase = true) -> com.example.R.drawable.img_book_mock_1782807364370
        name.contains("course", ignoreCase = true) -> com.example.R.drawable.img_course_mock_1782807342722
        else -> com.example.R.drawable.img_hero_banner_1782807308185
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // High-quality generated image background
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = drawableResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        // Dark modern overlay gradient for literature readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.25f),
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Title and Subtitle Overlay
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Icon(
                imageVector = when {
                    name.contains("course", ignoreCase = true) -> Icons.Default.MenuBook
                    name.contains("book", ignoreCase = true) -> Icons.Default.Book
                    name.contains("author", ignoreCase = true) -> Icons.Default.Portrait
                    name.contains("blog", ignoreCase = true) -> Icons.Default.Article
                    else -> Icons.Default.School
                },
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun CustomVideoPlayer(
    title: String,
    duration: String,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF5C5C5C), Color(0xFF1C1C1C))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isPlaying) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "লেকচার চলছে... ($duration)",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    IconButton(
                        onClick = onTogglePlay,
                        modifier = Modifier
                            .size(64.dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(32.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Lecture",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun NewsletterSection(
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var subscribed by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.MailOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "আমাদের সাহিত্য পত্রে যুক্ত হোন",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "নতুন নতুন বই প্রকাশ, কোর্সের মেগা ছাড় এবং আকর্ষণীয় সাহিত্য আলোচনার আপডেট মেইলে পেতে সাবস্ক্রাইব করুন।",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (subscribed) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2B8A3E), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "সাবস্ক্রিপশন সফল হয়েছে! ধন্যবাদ।",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("আপনার ইমেইল অ্যাড্রেস লিখুন", fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (email.contains("@")) {
                            subscribed = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("সাবস্ক্রাইব করুন", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FooterSection(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            .padding(top = 32.dp, bottom = 24.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Brand Identity Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "সবার জন্য বাংলা স্কুল",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )
        }

        Text(
            text = "বাঙালির মননশীল সাহিত্যচর্চা, ভাষা শিক্ষা ও সৃজনশীলতার ডিজিটাল মহাফেজখানা।",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(max = 320.dp)
                .padding(bottom = 24.dp),
            lineHeight = 18.sp
        )

        // Social Media Icons Row with canvas drawing for custom premium social branding
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            SocialMediaIcon(
                platformName = "Facebook",
                onClick = {},
                gradientColors = listOf(Color(0xFF1877F2), Color(0xFF0D529E))
            ) {
                // Draw white F
                drawLine(
                    color = Color.White,
                    start = Offset(size.width * 0.58f, size.height * 0.25f),
                    end = Offset(size.width * 0.58f, size.height * 0.80f),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(size.width * 0.45f, size.height * 0.45f),
                    end = Offset(size.width * 0.70f, size.height * 0.45f),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(size.width * 0.50f, size.height * 0.25f),
                    end = Offset(size.width * 0.60f, size.height * 0.25f),
                    strokeWidth = 5f
                )
            }

            SocialMediaIcon(
                platformName = "YouTube",
                onClick = {},
                gradientColors = listOf(Color(0xFFFF0000), Color(0xFFC00000))
            ) {
                // Draw YouTube triangle
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size.width * 0.42f, size.height * 0.35f)
                    lineTo(size.width * 0.65f, size.height * 0.50f)
                    lineTo(size.width * 0.42f, size.height * 0.65f)
                    close()
                }
                drawPath(path = path, color = Color.White)
            }

            SocialMediaIcon(
                platformName = "Instagram",
                onClick = {},
                gradientColors = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFF77737))
            ) {
                // Draw Instagram Outline Camera and Dot
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(size.width * 0.3f, size.height * 0.3f),
                    size = androidx.compose.ui.geometry.Size(size.width * 0.4f, size.height * 0.4f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                )
                drawCircle(
                    color = Color.White,
                    radius = size.width * 0.09f,
                    center = Offset(size.width * 0.5f, size.height * 0.5f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                )
                drawCircle(
                    color = Color.White,
                    radius = size.width * 0.03f,
                    center = Offset(size.width * 0.62f, size.height * 0.38f)
                )
            }

            SocialMediaIcon(
                platformName = "X / Twitter",
                onClick = {},
                gradientColors = listOf(Color(0xFF0F1419), Color(0xFF272C30))
            ) {
                // Draw stylized X
                drawLine(
                    color = Color.White,
                    start = Offset(size.width * 0.32f, size.height * 0.32f),
                    end = Offset(size.width * 0.68f, size.height * 0.68f),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(size.width * 0.68f, size.height * 0.32f),
                    end = Offset(size.width * 0.32f, size.height * 0.68f),
                    strokeWidth = 2.5f
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f))
        Spacer(modifier = Modifier.height(24.dp))

        // Quick Links Grid Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "প্রধান সূচি",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                FooterLink(text = "হোম পেজ", onClick = { onNavigate("home") })
                FooterLink(text = "সাহিত্য কোর্সসমূহ", onClick = { onNavigate("courses") })
                FooterLink(text = "বই ও ই-লাইব্রেরি", onClick = { onNavigate("books") })
                FooterLink(text = "বরেণ্য লেখকবৃন্দ", onClick = { onNavigate("authors") })
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "রিসোর্স ও তথ্য",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                FooterLink(text = "সাহিত্য ব্লগ ও কলাম", onClick = { onNavigate("blog") })
                FooterLink(text = "আমাদের সম্পর্কে", onClick = { onNavigate("about") })
                FooterLink(text = "যোগাযোগ করুন", onClick = { onNavigate("contact") })
                FooterLink(text = "সাহিত্য কুইজ গেম", onClick = { onNavigate("play") })
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "যোগাযোগ মাধ্যম",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "info@banglaschool.com",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "+৮৮০ ৯৬১২-৩৪৫৬৭৮",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "রমনা, ঢাকা, বাংলাদেশ",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f))
        Spacer(modifier = Modifier.height(16.dp))

        // Copyright and bottom meta info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "© ২০২৬ বাংলা স্কুল। সর্বস্বত্ব সংরক্ষিত।",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "শর্তাবলী",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { }
                )
                Text(
                    text = "গোপনীয়তা নীতি",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { }
                )
            }
        }
    }
}

@Composable
fun FooterLink(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    )
}

@Composable
fun SocialMediaIcon(
    platformName: String,
    onClick: () -> Unit,
    gradientColors: List<Color>,
    drawContent: androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else if (isHovered) 1.15f else 1.0f,
        label = "social_scale"
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .background(Brush.linearGradient(colors = gradientColors))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSheet(
    itemName: String,
    price: Double,
    onPaymentSuccess: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf("bKash") } // bKash, Nagad, Rocket, Card
    var isPaying by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "সুরক্ষিত পেমেন্ট গেটওয়ে",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ক্রয় করুন: $itemName",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "মোট মূল্য: ৳${price.toInt()}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "পেমেন্ট মাধ্যম বেছে নিন:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Payment Option Buttons
            val methods = listOf(
                Triple("bKash", "bKash (বিকাশ)", Color(0xFFE2136E)),
                Triple("Nagad", "Nagad (নগদ)", Color(0xFFF15A22)),
                Triple("Rocket", "Rocket (রকেট)", Color(0xFF8C3494)),
                Triple("SSLCommerz", "SSLCommerz / Card", Color(0xFF005691))
            )

            methods.forEach { (key, label, color) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            if (selectedMethod == key) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.5f
                            ),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.5.dp,
                            if (selectedMethod == key) MaterialTheme.colorScheme.primary else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedMethod = key }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(color, RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedMethod == key) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    RadioButton(
                        selected = selectedMethod == key,
                        onClick = { selectedMethod = key }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isPaying) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("পেমেন্ট যাচাই করা হচ্ছে...", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            } else {
                Button(
                    onClick = {
                        isPaying = true
                        // Simulate payment gateway API delay
                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            onPaymentSuccess(selectedMethod)
                            isPaying = false
                        }, 1500)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("৳${price.toInt()} পেমেন্ট সম্পন্ন করুন", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Helper for Bengali Digits to support modern literary aesthetic
fun String.toBengaliDigits(): String {
    val englishDigits = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val bengaliDigits = listOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    var result = this
    for (i in 0..9) {
        result = result.replace(englishDigits[i], bengaliDigits[i])
    }
    return result
}

@Composable
fun ShadcnCourseCard(
    course: Course,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onWishlistClick: (() -> Unit)? = null,
    showBadge: Boolean = true,
    showMetadata: Boolean = true,
    showDivider: Boolean = true,
    actionContent: @Composable (RowScope.() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Dynamic scale-up and shadow-lifting animations
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else if (isHovered) 1.03f else 1.0f,
        animationSpec = tween(durationMillis = 250),
        label = "scale"
    )
    
    val shadowElevation by animateDpAsState(
        targetValue = if (isPressed) 1.dp else if (isHovered) 6.dp else 1.dp,
        animationSpec = tween(durationMillis = 250),
        label = "shadow"
    )

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(shadowElevation, RoundedCornerShape(12.dp))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                // Course Thumbnail using MockImage
                MockImage(
                    name = course.thumbnail,
                    title = course.title,
                    subtitle = course.instructor,
                    height = 130
                )
                
                // Badges overlay
                if (showBadge) {
                    // Category Badge (Left)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f),
                                RoundedCornerShape(6.dp)
                            )
                            .border(0.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "সাহিত্য ধারা",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    // Discount Badge (Right)
                    if (course.discountPrice != null && course.price > 0) {
                        val discountPercent = (((course.price - course.discountPrice) / course.price) * 100).toInt()
                        if (discountPercent > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(
                                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.95f),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .border(0.5.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${discountPercent}% ছাড়".toBengaliDigits(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }

                // Wishlist Toggle Heart Button (Bottom-Right overlay of Thumbnail)
                if (onWishlistClick != null) {
                    val heartScale by animateFloatAsState(
                        targetValue = if (isFavorite) 1.25f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "heartScale"
                    )
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(32.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clickable(
                                onClick = onWishlistClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(16.dp)
                                .graphicsLayer {
                                    scaleX = heartScale
                                    scaleY = heartScale
                                }
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                // Course Title
                Text(
                    text = course.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Instructor Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = course.instructor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (showMetadata) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Metadata Row: Duration & Lessons Count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = course.duration.toBengaliDigits(),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.width(10.dp))
                            
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${course.lessonsCount}টি লেকচার".toBengaliDigits(),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Rating Badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = Color(0xFFF1C40F)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = course.rating.toString().toBengaliDigits(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                if (showDivider) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    )
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Price & Action Block
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pricing Block
                    Column {
                        if (course.discountPrice != null) {
                            Text(
                                text = "৳${course.price.toInt()}".toBengaliDigits(),
                                fontSize = 11.sp,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = "৳${course.discountPrice.toInt()}".toBengaliDigits(),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = "৳${course.price.toInt()}".toBengaliDigits(),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Right slot (Action content)
                    if (actionContent != null) {
                        Row(content = actionContent)
                    } else {
                        // Beautiful default Shadcn UI action button
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "শুরু করুন",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffsetX"
    )

    val baseColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
    val highlightColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)

    return this.background(
        brush = Brush.linearGradient(
            colors = listOf(baseColor, highlightColor, baseColor),
            start = Offset(startOffsetX, startOffsetX),
            end = Offset(startOffsetX + 300f, startOffsetX + 300f)
        )
    )
}

@Composable
fun ShadcnCourseCardSkeleton(
    modifier: Modifier = Modifier,
    showMetadata: Boolean = true,
    showDivider: Boolean = true
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(12.dp))
    ) {
        Column {
            // Thumbnail Skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .shimmerEffect()
            )

            Column(modifier = Modifier.padding(14.dp)) {
                // Title Line 1 Skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Title Line 2 Skeleton
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Instructor Row Skeleton
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                }

                if (showMetadata) {
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Metadata Row Skeletons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .shimmerEffect()
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .shimmerEffect()
                            )
                        }

                        // Rating
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .shimmerEffect()
                            )
                        }
                    }
                }

                if (showDivider) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Price & Action Block Skeletons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pricing Block
                    Column {
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(8.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(65.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmerEffect()
                        )
                    }

                    // Action Button
                    Box(
                        modifier = Modifier
                            .width(85.dp)
                            .height(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Composable
fun ShadcnBookCard(
    book: Book,
    onClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    modifier: Modifier = Modifier,
    isAddedToCart: Boolean = false,
    showBadges: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Smooth Shadcn micro-interactions
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else if (isHovered) 1.03f else 1.0f,
        animationSpec = tween(durationMillis = 200),
        label = "book_scale"
    )
    val shadowElevation by animateDpAsState(
        targetValue = if (isPressed) 1.dp else if (isHovered) 6.dp else 1.5.dp,
        animationSpec = tween(durationMillis = 200),
        label = "book_shadow"
    )

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isHovered) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            }
        ),
        shape = RoundedCornerShape(12.dp),
        interactionSource = interactionSource,
        modifier = modifier
            .width(165.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(shadowElevation, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Book cover container with category and discount badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                MockImage(
                    name = book.coverUrl,
                    title = book.title,
                    subtitle = book.authorName,
                    height = 160
                )

                if (showBadges) {
                    // Category Tag Badge (Top-Left)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.92f),
                                RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = 0.5.dp,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = book.category,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    // Top-Right: Rating Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                Color.Black.copy(alpha = 0.65f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFF1C40F),
                                modifier = Modifier.size(9.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${book.rating}".toBengaliDigits(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    // Best Seller overlay tag
                    if (book.isBestSeller) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                                .background(
                                    Color(0xFFE8590C), // Pure brand orange
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "বেস্টসেলার",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Book Details padding container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Book Title
                Text(
                    text = book.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))

                // Author Name
                Text(
                    text = book.authorName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Price and discount Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val currentPriceVal = (book.discountPrice ?: book.price).toInt()
                    
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "৳${currentPriceVal}".toBengaliDigits(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            if (book.discountPrice != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "৳${book.price.toInt()}".toBengaliDigits(),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    style = androidx.compose.ui.text.TextStyle(
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                )
                            }
                        }
                    }

                    // Format Badge indicator: Ebook vs Physical
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (book.isEbook) {
                            Icon(
                                imageVector = Icons.Default.TabletAndroid,
                                contentDescription = "Ebook",
                                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                                modifier = Modifier.size(11.dp)
                            )
                        }
                        if (book.isPhysical) {
                            if (book.isEbook) Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = "Physical Book",
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Reusable Shadcn style "Add to Cart" CTA button in Light Orange Theme
                Button(
                    onClick = onAddToCartClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    colors = if (isAddedToCart) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary, // Vibrant Orange
                            contentColor = Color.White
                        )
                    },
                    border = if (isAddedToCart) {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isAddedToCart) Icons.Default.CheckCircle else Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAddedToCart) "কার্টে রয়েছে" else "কার্টে যোগ করুন",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShadcnPersistentHeaderSearchBar(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Perform real-time filtered results for Books, Courses, and Authors
    val filteredBooks = remember(query, viewModel.books) {
        if (query.isBlank()) emptyList() else {
            viewModel.books.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.authorName.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }.take(3)
        }
    }

    val filteredCourses = remember(query, viewModel.courses) {
        if (query.isBlank()) emptyList() else {
            viewModel.courses.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.instructor.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }.take(3)
        }
    }

    val filteredAuthors = remember(query, viewModel.authors) {
        if (query.isBlank()) emptyList() else {
            viewModel.authors.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.bio.contains(query, ignoreCase = true) ||
                it.achievements.contains(query, ignoreCase = true)
            }.take(3)
        }
    }

    val hasSuggestions = filteredBooks.isNotEmpty() || filteredCourses.isNotEmpty() || filteredAuthors.isNotEmpty()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        text = "বই, কোর্স বা লেখক খুঁজুন...",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f)
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .onFocusChanged { isFocused = it.isFocused }
            )

            // Popup Suggestion Dropdown List (floating elegantly over other contents)
            if (isFocused && query.isNotEmpty()) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = androidx.compose.ui.unit.IntOffset(0, 140), // Perfectly positioned below the search input
                    onDismissRequest = { isFocused = false },
                    properties = PopupProperties(focusable = false)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .shadow(12.dp, RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp)
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = 8.dp)
                        ) {
                            if (!hasSuggestions) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.FindInPage,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "কোনো ফলাফল পাওয়া যায়নি",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            } else {
                                // --- Matching Books Section ---
                                if (filteredBooks.isNotEmpty()) {
                                    SuggestionGroupHeader(title = "বইসমূহ", icon = Icons.Default.Book)
                                    filteredBooks.forEach { book ->
                                        SuggestionRow(
                                            title = book.title,
                                            subtitle = book.authorName,
                                            badge = book.category,
                                            onClick = {
                                                viewModel.navigateTo(Screen.BookDetails(book.id))
                                                query = ""
                                                focusManager.clearFocus()
                                            }
                                        )
                                    }
                                }

                                // --- Matching Courses Section ---
                                if (filteredCourses.isNotEmpty()) {
                                    SuggestionGroupHeader(title = "কোর্সসমূহ", icon = Icons.Default.School)
                                    filteredCourses.forEach { course ->
                                        SuggestionRow(
                                            title = course.title,
                                            subtitle = course.instructor,
                                            badge = "কোর্স",
                                            onClick = {
                                                viewModel.navigateTo(Screen.CourseDetails(course.id))
                                                query = ""
                                                focusManager.clearFocus()
                                            }
                                        )
                                    }
                                }

                                // --- Matching Authors Section ---
                                if (filteredAuthors.isNotEmpty()) {
                                    SuggestionGroupHeader(title = "লেখকবৃন্দ", icon = Icons.Default.Groups)
                                    filteredAuthors.forEach { author ->
                                        SuggestionRow(
                                            title = author.name,
                                            subtitle = author.bio,
                                            badge = "সাহিত্যিক",
                                            onClick = {
                                                viewModel.navigateTo(Screen.AuthorDetails(author.id))
                                                query = ""
                                                focusManager.clearFocus()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionGroupHeader(
    title: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun SuggestionRow(
    title: String,
    subtitle: String,
    badge: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = badge,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

