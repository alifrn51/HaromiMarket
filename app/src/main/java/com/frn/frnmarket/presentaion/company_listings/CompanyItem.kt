package com.frn.frnmarket.presentaion.company_listings

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.frn.frnmarket.domain.model.CompanyListing

@Composable
fun CompanyItem(
    company: CompanyListing,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        Column(Modifier.weight(1f)) {

            Row(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = company.name,
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = company.exchange,
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Light
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "(${company.symbol})", color = MaterialTheme.colors.onBackground)

        }

    }


}