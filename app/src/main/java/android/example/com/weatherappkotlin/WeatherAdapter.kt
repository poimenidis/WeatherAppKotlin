package android.example.com.weatherappkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class WeatherAdapter(context: Context,list: ArrayList<Weather>) : BaseAdapter() {
    var sList = list
    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return sList.size
    }

    override fun getItem(position: Int): Any {
        return sList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.weather_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.day.text = sList[position].day
        viewHolder.temp.text = sList[position].temper

        if(sList.get(position).icon=="01d"||sList.get(position).icon=="01n")
            viewHolder.image.setImageResource(R.drawable.a01d)
        else if(sList.get(position).icon=="02d"||sList.get(position).icon=="02n")
            viewHolder.image.setImageResource(R.drawable.a02d)
        else if(sList.get(position).icon=="03d"||sList.get(position).icon=="03n")
            viewHolder.image.setImageResource(R.drawable.a03d)
        else if(sList.get(position).icon=="04d"||sList.get(position).icon=="04n")
            viewHolder.image.setImageResource(R.drawable.a01d)
        else if(sList.get(position).icon=="09d"||sList.get(position).icon=="09n")
            viewHolder.image.setImageResource(R.drawable.a09d)
        else if(sList.get(position).icon=="10d"||sList.get(position).icon=="10n")
            viewHolder.image.setImageResource(R.drawable.a10d)
        else if(sList.get(position).icon=="11d"||sList.get(position).icon=="11n")
            viewHolder.image.setImageResource(R.drawable.a11d)
        else if(sList.get(position).icon=="13d"||sList.get(position).icon=="13n")
            viewHolder.image.setImageResource(R.drawable.a13n)
        else if(sList.get(position).icon=="50d"||sList.get(position).icon=="50n")
            viewHolder.image.setImageResource(R.drawable.a50d)

        return view
    }

    fun clear(){
        sList.clear()
    }
}

private class ViewHolder(view: View?) {
    public val day: TextView
    public val temp: TextView
    public val image: ImageView

    init {
        this.day = view?.findViewById(R.id.day) as TextView
        this.temp = view?.findViewById(R.id.temp) as TextView
        this.image = view?.findViewById(R.id.imageView) as ImageView
    }
}

