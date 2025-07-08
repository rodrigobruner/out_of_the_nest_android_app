package app.outofthenest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import app.outofthenest.R;
import app.outofthenest.models.FamilyMember;

public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.ViewHolder> {

    private List<FamilyMember> familyMembers;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public FamilyMemberAdapter(List<FamilyMember> familyMembers, OnDeleteClickListener listener) {
        this.familyMembers = familyMembers;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_family_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FamilyMember member = familyMembers.get(position);
        holder.typeTextView.setText(member.getType());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.birthTextView.setText(sdf.format(member.getBirth()));

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return familyMembers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeTextView;
        TextView birthTextView;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.tcv_type);
            birthTextView = itemView.findViewById(R.id.txv_birth_date);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}